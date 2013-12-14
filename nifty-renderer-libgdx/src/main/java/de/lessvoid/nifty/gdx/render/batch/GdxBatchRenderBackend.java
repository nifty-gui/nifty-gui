package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.BufferUtils;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.gdx.render.GdxMouseCursor;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.ObjectPool.Factory;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 *
 * LibGDX & OpenGL ES Implementation of the {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} Interface. This
 * implementation will be the most backwards-compatible because it doesn't use any functions beyond OpenGL ES 1.0.
 * It is suitable for both desktop and mobile devices.
 *
 * Based on void's {@link de.lessvoid.nifty.renderer.lwjgl.render.batch.LwjglBatchRenderBackend} class.
 *
 * The idea of the BatchRenderDevice is to remove as much state changes as possible from rendering a Nifty scene. The
 * way this is done is through these mechanism:
 *
 * 1) A texture atlas is used to store all textures in a single large texture. The actual texture size is provided
 *    by the user but will usually be around 2048x2048 pixel. A BatchRenderBackend has to provide mechanism to create
 *    that texture and a way to load an image and put it at a given position into that big texture.
 *
 * 2) The only actual render data that Nifty will call this BatchRenderBackend with are textured quads with individual
 *    vertex colors. Nifty will provide the texture coordinates which will always be relative to the texture atlas
 *    created before. A BatchRenderBackend implementation should cache or send these quads to the GPU to be later
 *    rendered in a single draw call when Nifty calls the render() method of a BatchRenderBackend implementation.
 *
 * 3) The batch size, e.g. how many quads will fit into a single batch is decided by the BatchRenderBackend
 *    implementation. If a quad does not fit into the current batch the BatchRenderBackend implementation might create
 *    a new batch automatically.
 *
 * 4) Nifty will only start a new batch when the BlendMode changes.
 *
 * 5) Since OpenGL ES doesn't support the idea of GL_QUADS, we have to tessellate the quads into two connected
 *    triangles by duplicating the two shared vertices, so that we can use GL_TRIANGLES instead. So we have a total of
 *    6 vertices per quad (2 connected triangles forming a quad), but the performance hit is more than offset by the
 *    ability of the GPU to handle triangles much more efficiently than quads. GL_TRIANGLES submits the triangles'
 *    vertices in batches to the GPU via a vertex buffer in what is called a triangle list; that is, the triangles are
 *    treated as separate entities by the GPU, so it doesn't automatically try and join all of them together, as it
 *    would with GL_TRIANGLE_STRIP or GL_TRIANGLE_FAN. This is of great benefit for rendering quads, since we want each
 *    quad to be distinct. We then manually tell the GPU to connect every two consecutive triangles together by
 *    specifying two duplicate or shared vertices per quad. This way, two consecutive triangles are pieced together
 *    seamlessly, to create the illusion of a quad, while the next triangle will be seen as a separate entity,
 *    effectively creating a separate quad every other triangle. There are more advanced methods for rendering quads in
 *    OpenGL ES, however, this implementation seeks to be as backwards-compatible as possible, since it only uses
 *    OpenGL ES 1.0.
 */
public class GdxBatchRenderBackend implements BatchRenderBackend {
  private static Logger log = Logger.getLogger(GdxBatchRenderBackend.class.getName());
  private int atlasTextureId;
  @Nonnull
  private final ObjectPool<Batch> batchPool;
  private Batch currentBatch;
  private final List<Batch> batches = new ArrayList<Batch>();
  private ByteBuffer initialData;
  private boolean fillRemovedTexture =
      Boolean.parseBoolean(System.getProperty(GdxBatchRenderBackend.class.getName() + ".fillRemovedTexture", "false"));
  private GdxMouseCursor mouseCursor;
  private ArrayMap<String, GdxMouseCursor> mouseCursors;

  public GdxBatchRenderBackend() {
    mouseCursors = new ArrayMap<String, GdxMouseCursor>();
    batchPool = new ObjectPool<Batch>(new Factory<Batch>() {
      @Nonnull
      @Override
      public Batch createNew() {
        return new Batch();
      }
    });
    initializeOpenGL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
    // nothing to do
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWidth() {
    return Gdx.graphics.getWidth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getHeight() {
    return Gdx.graphics.getHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beginFrame() {
    log.fine("beginFrame()");
    for (int i=0; i<batches.size(); i++) {
      batchPool.free(batches.get(i));
    }
    batches.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endFrame() {
    log.fine("endFrame");
    checkGLError();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    log.fine("clear()");
    Gdx.gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public MouseCursor createMouseCursor(final String filename, final int hotspotX, final int hotspotY) throws IOException {
    // Prevent many creations of the same mouse cursor. TODO Nifty should be doing this automatically.
    if (mouseCursors.containsKey(filename)) {
      return mouseCursors.get(filename);
    }
    try {
      GdxMouseCursor mouseCursor = new GdxMouseCursor(new GdxBatchRenderImage(filename), hotspotX, hotspotY);
      mouseCursors.put(filename, mouseCursor);
      return mouseCursor;
    } catch (Exception e) {
      log.log(Level.WARNING, "problems creating mouse cursor [" + filename + "]", e);
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enableMouseCursor(final MouseCursor mouseCursor) {
    if (mouseCursor instanceof GdxMouseCursor) {
      this.mouseCursor = (GdxMouseCursor) mouseCursor;
      this.mouseCursor.enable();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      mouseCursor.disable();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createAtlasTexture(final int width, final int height) {
    try {
      createAtlasTexture(width, height, false);

      initialData = BufferUtils.newByteBuffer(width * height * 4);
      for (int i=0; i<width*height; i++) {
        initialData.put((byte) 0x00);
        initialData.put((byte) 0xff);
        initialData.put((byte) 0x00);
        initialData.put((byte) 0xff);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearAtlasTexture(final int width, final int height) {
    initialData.rewind();
    bind(atlasTextureId);
    Gdx.gl10.glTexImage2D(
            GL10.GL_TEXTURE_2D,
            0,
            GL10.GL_RGBA,
            width,
            height,
            0,
            GL10.GL_RGBA,
            GL10.GL_UNSIGNED_BYTE,
            initialData);
    checkGLError();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Image loadImage(final String filename) {
    try {
      return new GdxBatchRenderImage(filename);
    } catch (Exception e) {
      log.log(Level.WARNING, "problems loading image [" + filename + "]", e);
      return new GdxBatchRenderImage(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addImageToTexture(@Nonnull final Image image, final int x, final int y) {
    GdxBatchRenderImage gdxImage = (GdxBatchRenderImage) image;
    if (gdxImage == null || gdxImage.getWidth() == 0 || gdxImage.getHeight() == 0) return;
    bind(atlasTextureId);
    Gdx.gl10.glTexSubImage2D(
            GL10.GL_TEXTURE_2D,
            0,
            x,
            y,
            image.getWidth(),
            image.getHeight(),
            GL10.GL_RGBA,
            GL10.GL_UNSIGNED_BYTE,
            gdxImage.asByteBuffer());
    checkGLError();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beginBatch(final BlendMode blendMode) {
    batches.add(batchPool.allocate());
    currentBatch = batches.get(batches.size() - 1);
    currentBatch.begin(blendMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addQuad(
      final float x,
      final float y,
      final float width,
      final float height,
      @Nonnull final Color color1,
      @Nonnull final Color color2,
      @Nonnull final Color color3,
      @Nonnull final Color color4,
      final float textureX,
      final float textureY,
      final float textureWidth,
      final float textureHeight) {
    if (!currentBatch.canAddQuad()) {
      beginBatch(currentBatch.getBlendMode());
    }
    currentBatch.addQuadInternal(x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth, textureHeight);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int render() {
    bind(atlasTextureId);

    Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
    Gdx.gl10.glEnable(GL10.GL_BLEND);
    Gdx.gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    Gdx.gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
    Gdx.gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

    for (int i=0; i<batches.size(); i++) {
      Batch batch = batches.get(i);
      batch.render();
    }

    Gdx.gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    Gdx.gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
    Gdx.gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    Gdx.gl10.glDisable(GL10.GL_BLEND);
    Gdx.gl10.glDisable(GL10.GL_TEXTURE_2D);

    return batches.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeFromTexture(@Nonnull final Image image, final int x, final int y, final int w, final int h) {
    // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
    // texture atlas when individual textures are removed. If necessary this can be enabled with a system property.
    if (!fillRemovedTexture) {
      return;
    }
    ByteBuffer initialData = BufferUtils.newByteBuffer(image.getWidth()*image.getHeight()*4);
    for (int i=0; i<image.getWidth()*image.getHeight(); i++) {
      initialData.put((byte) 0xff);
      initialData.put((byte) 0x00);
      initialData.put((byte) 0x00);
      initialData.put((byte) 0xff);
    }
    initialData.rewind();
    bind(atlasTextureId);
    Gdx.gl10.glTexSubImage2D(
            GL10.GL_TEXTURE_2D,
            0,
            x,
            y,
            w,
            h,
            GL10.GL_RGBA,
            GL10.GL_UNSIGNED_BYTE,
            initialData);
  }

  // internal implementations

  private void initializeOpenGL() {
    Gdx.gl10.glViewport(0, 0, getWidth(), getHeight());
    Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
    Gdx.gl10.glLoadIdentity();
    Gdx.gl10.glOrthof(0.0f, (float) getWidth(), (float) getHeight(), 0.0f, -9999.0f, 9999.0f);
    Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
    Gdx.gl10.glLoadIdentity();
    Gdx.gl10.glDisable(GL10.GL_DEPTH_TEST);
    Gdx.gl10.glEnable(GL10.GL_BLEND);
    Gdx.gl10.glDisable(GL10.GL_CULL_FACE);
    Gdx.gl10.glEnable(GL10.GL_ALPHA_TEST);
    Gdx.gl10.glAlphaFunc(GL10.GL_NOTEQUAL, 0);
    Gdx.gl10.glDisable(GL10.GL_LIGHTING);
    Gdx.gl10.glDisable(GL10.GL_TEXTURE_2D);
    Gdx.gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
    Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
    // Enable exact pixelization for 2D rendering
    // See: http://www.opengl.org/archives/resources/faq/technical/transformations.htm#tran0030
    Gdx.gl10.glTranslatef (0.375f, 0.375f, 0.0f);
  }

  private void checkGLError() {
    int error= Gdx.gl10.glGetError();
    if (error != GL10.GL_NO_ERROR) {
      String glerrmsg;
      switch (error) {
        case GL10.GL_INVALID_ENUM:
          glerrmsg = "Invalid enum";
          break;
        case GL10.GL_INVALID_VALUE:
          glerrmsg = "Invalid value";
          break;
        case GL10.GL_INVALID_OPERATION:
          glerrmsg = "Invalid operation";
          break;
        case GL10.GL_STACK_OVERFLOW:
          glerrmsg = "Stack overflow";
          break;
        case GL10.GL_STACK_UNDERFLOW:
          glerrmsg = "Stack underflow";
          break;
        case GL10.GL_OUT_OF_MEMORY:
          glerrmsg = "Out of memory";
          break;
        default:
          glerrmsg = "";
          break;
      }
      log.warning("Error: (" + error + ") " + glerrmsg);
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void createAtlasTexture(final int width, final int height, final boolean filter) throws Exception {
    atlasTextureId = createTextureId();
    int minFilter = GL10.GL_NEAREST;
    int magFilter = GL10.GL_NEAREST;
    if (filter) {
      minFilter = GL10.GL_LINEAR_MIPMAP_LINEAR;
      magFilter = GL10.GL_NEAREST;
    }
    bind(atlasTextureId);

    IntBuffer temp = BufferUtils.newIntBuffer(16);
    Gdx.gl10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, temp);
    checkGLError();

    int max = temp.get(0);
    if ((width > max) || (height > max)) {
      throw new Exception("Attempt to allocate a texture to big for the current hardware");
    }
    if (width < 0) {
      log.warning("Attempt to allocate a texture with negative width");
      return;
    }
    if (height < 0) {
      log.warning("Attempt to allocate a texture with negative height");
      return;
    }

    Gdx.gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
    Gdx.gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);

    checkGLError();

    ByteBuffer initialData = BufferUtils.newByteBuffer(width * height * 4);
    for (int i=0; i<width*height*4; i++) {
      initialData.put((byte) 0x80);
    }
    initialData.rewind();
    Gdx.gl10.glTexImage2D(
          GL10.GL_TEXTURE_2D,
          0,
          GL10.GL_RGBA,
          width,
          height,
          0,
          GL10.GL_RGBA,
          GL10.GL_UNSIGNED_BYTE,
          initialData);
    checkGLError();
  }

  private void bind(int textureId) {
    Gdx.gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    checkGLError();
  }

  private int createTextureId() {
    IntBuffer tmp = BufferUtils.newIntBuffer(1);
    Gdx.gl10.glGenTextures(1, tmp);
    checkGLError();
    return tmp.get(0);
 }

  /**
   * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
   *
   * Based on void's {@link de.lessvoid.nifty.renderer.lwjgl.render.batch.LwjglBatchRenderBackend.Batch} class.
   *
   * This class helps us to manage the batch data. We'll keep a bunch of instances of this class around that will be
   * reused when needed. Each Batch instance provides room for a certain amount of vertices and we'll use a new Batch
   * when we exceed this amount of data.
   */
  private class Batch {
    private final static int VERTICES_PER_QUAD = 6;
    private final static int ATTRIBUTES_PER_VERTEX = 8;
    private final static int BYTES_PER_ATTRIBUTE = 4;
    private final static int PRIMITIVE_SIZE = VERTICES_PER_QUAD * ATTRIBUTES_PER_VERTEX;
    private final static int STRIDE = ATTRIBUTES_PER_VERTEX * BYTES_PER_ATTRIBUTE;
    private final int SIZE = 64*1024; // 64k
    private final FloatBuffer vertexBuffer;
    private int primitiveCount;
    @Nonnull
    private float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
    private BlendMode blendMode = BlendMode.BLEND;

    private Batch() {
      vertexBuffer = BufferUtils.newFloatBuffer(SIZE);
    }

    public void begin(final BlendMode blendMode) {
      this.blendMode = blendMode;
      primitiveCount = 0;
      vertexBuffer.clear();
    }

    public BlendMode getBlendMode() {
      return blendMode;
    }

    public void render() {
      if (primitiveCount == 0) return; // Attempting to render with an empty vertex buffer crashes the program.

      if (blendMode.equals(BlendMode.BLEND)) {
        Gdx.gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
      } else if (blendMode.equals(BlendMode.MULIPLY)) {
        Gdx.gl10.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ZERO);
      }

      vertexBuffer.flip();
      vertexBuffer.position(0);
      Gdx.gl10.glVertexPointer(2, GL10.GL_FLOAT, STRIDE, vertexBuffer);

      vertexBuffer.position(2);
      Gdx.gl10.glColorPointer(4, GL10.GL_FLOAT, STRIDE, vertexBuffer);

      vertexBuffer.position(6);
      Gdx.gl10.glTexCoordPointer(2, GL10.GL_FLOAT, STRIDE, vertexBuffer);

      // GL_QUADS is not supported in libGDX / OpenGL ES, so we'll use GL_TRIANGLES instead.
      Gdx.gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, primitiveCount * VERTICES_PER_QUAD);
    }

    public boolean canAddQuad() {
      return ((primitiveCount + 1) * PRIMITIVE_SIZE) < SIZE;
    }

    private void addQuadInternal(
        final float x,
        final float y,
        final float width,
        final float height,
        @Nonnull final Color color1,
        @Nonnull final Color color2,
        @Nonnull final Color color3,
        @Nonnull final Color color4,
        final float textureX,
        final float textureY,
        final float textureWidth,
        final float textureHeight) {
      int bufferIndex = 0;

      // Quad, tessellated into a triangle list with clockwise-winded vertices - (0,1,2), (0,2,3), (0,3,3)
      //
      // 0---1
      // | \ |
      // |  \|
      // 3---2

      // Triangle 1 (0,1,2)

      // 0
      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color1.getRed();
      primitiveBuffer[bufferIndex++] = color1.getGreen();
      primitiveBuffer[bufferIndex++] = color1.getBlue();
      primitiveBuffer[bufferIndex++] = color1.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex++] = textureY;

      // 1
      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color2.getRed();
      primitiveBuffer[bufferIndex++] = color2.getGreen();
      primitiveBuffer[bufferIndex++] = color2.getBlue();
      primitiveBuffer[bufferIndex++] = color2.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex++] = textureY;

      // 2
      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color4.getRed();
      primitiveBuffer[bufferIndex++] = color4.getGreen();
      primitiveBuffer[bufferIndex++] = color4.getBlue();
      primitiveBuffer[bufferIndex++] = color4.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;

      // Triangle 2 (0,2,3)

      // 0
      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color1.getRed();
      primitiveBuffer[bufferIndex++] = color1.getGreen();
      primitiveBuffer[bufferIndex++] = color1.getBlue();
      primitiveBuffer[bufferIndex++] = color1.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex++] = textureY;

      // 2
      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color4.getRed();
      primitiveBuffer[bufferIndex++] = color4.getGreen();
      primitiveBuffer[bufferIndex++] = color4.getBlue();
      primitiveBuffer[bufferIndex++] = color4.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;

      // 3
      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color3.getRed();
      primitiveBuffer[bufferIndex++] = color3.getGreen();
      primitiveBuffer[bufferIndex++] = color3.getBlue();
      primitiveBuffer[bufferIndex++] = color3.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;

      vertexBuffer.put(primitiveBuffer);
      primitiveCount++;
    }
  }
}
