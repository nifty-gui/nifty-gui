package de.lessvoid.nifty.renderer.jogl.render.batch;

import com.jogamp.common.nio.Buffers;
import de.lessvoid.nifty.batch.TextureAtlasGenerator;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRendererTexture;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursor;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.Factory;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jogl BatchRenderBackend Implementation.
 *
 * @author void
 */
public class JoglBatchRenderBackend implements BatchRenderBackend {
  private static final Logger log = Logger.getLogger(JoglBatchRenderBackend.class.getName());
  private static final IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4 * 4);
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  @Nonnull
  private final ObjectPool<Batch> batchPool;
  private BatchRendererTexture atlasTexture;
  private final List<Batch> atlasBatches = new ArrayList<Batch>();
  private final Map<BatchRendererTexture, List<Batch>> batches = new HashMap<BatchRendererTexture, List<Batch>>();
  private final Map<BatchRendererTexture, Batch> currentBatches = new HashMap<BatchRendererTexture, Batch>();
  private final boolean fillRemovedTexture =
      Boolean.parseBoolean(System.getProperty(JoglBatchRenderBackend.class.getName() + ".fillRemovedTexture", "false"));
  private final GLU glu;

  public JoglBatchRenderBackend() {
    glu = GLU.createGLU();
    batchPool = new ObjectPool<Batch>(new Factory<Batch>() {
      @Nonnull
      @Override
      public Batch createNew() {
        return new Batch();
      }
    });
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public int getWidth() {
    if (viewportWidth == -1) {
      getViewport();
    }
    return viewportWidth;
  }

  @Override
  public int getHeight() {
    if (viewportHeight == -1) {
      getViewport();
    }
    return viewportHeight;
  }

  @Override
  public void beginFrame() {
    log.fine("beginFrame()");

    for (List<Batch> batchList : batches.values()) {
      for (Batch batch : batchList) {
        batchPool.free(batch);
      }
    }

    for (Batch batch : atlasBatches) {
      batchPool.free(batch);
    }

    batches.clear();
    atlasBatches.clear();
    currentBatches.clear();
  }

  @Override
  public void endFrame() {
    log.fine("endFrame");

    viewportWidth = -1;
    viewportHeight = -1;
    checkGLError();
  }

  @Override
  public void clear() {
    log.fine("clear()");

    final GL gl = GLContext.getCurrentGL();
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
  }

  @Override
  public MouseCursor createMouseCursor(
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    return new JoglMouseCursor(loadMouseCursor(filename, hotspotX, hotspotY));
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    // TODO implement this method later
  }

  @Override
  public void disableMouseCursor() {
    // TODO implement this method later
  }


  @Override
  public BatchRendererTexture createAtlasTexture(int width, int height) {
    ByteBuffer initialData = ByteBuffer.allocateDirect(width * height * 4);
    for (int i = 0; i < width * height * 4; i++) {
      initialData.put((byte) 0x80);
    }
    return createTexture(initialData, width, height, true);
  }

  @Override
  public BatchRendererTexture createFontTexture(@Nonnull final ByteBuffer data, int width, int height) {
    return createTexture(data, width, height, false);
  }


  private BatchRendererTexture createTexture(@Nonnull final ByteBuffer data, final int width, final int height, boolean atlas) {
    final int atlasTextureId;

    if (data.capacity() < width * height * 4) {
      log.severe("Atlas texture' buffer capacity is less than requested WIDTH x HEIGHT");
      return null;
    }

    try {
      data.rewind();
      atlasTextureId = createAtlasTexture(data, width, height, false, GL.GL_RGBA);
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
      return null;
    }

    BatchRendererTexture texture = new BatchRendererTexture() {
      private final TextureAtlasGenerator textureAtlasGenerator = new TextureAtlasGenerator(width, height);

      @Override
      public int getWidth() {
        return width;
      }

      @Override
      public int getHeight() {
        return height;
      }

      @Override
      public void bind() {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        gl.glBindTexture(GL2.GL_TEXTURE_2D, atlasTextureId);
        checkGLError();
      }

      @Override
      public TextureAtlasGenerator getGenerator() {
        return textureAtlasGenerator;
      }

      @Override
      public void dispose() {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        gl.glDeleteTextures(1, new int[] { atlasTextureId }, 0);
      }

      @Override
      public void clear() {
        data.rewind();
        final GL gl = GLContext.getCurrentGL();
        gl.glTexImage2D(
            GL.GL_TEXTURE_2D,
            0,
            4,
            width,
            height,
            0,
            GL.GL_RGBA,
            GL.GL_UNSIGNED_BYTE,
            data);
        checkGLError();
      }

      @Override
      public void addImageToTexture(@Nonnull Image image, int x, int y) {
        ImageImpl imageImpl = (ImageImpl) image;
        if (imageImpl.getWidth() == 0 ||
            imageImpl.getHeight() == 0) {
          return;
        }
        final GL gl = GLContext.getCurrentGL();
        bind();
        gl.glTexSubImage2D(
            GL.GL_TEXTURE_2D,
            0,
            x,
            y,
            image.getWidth(),
            image.getHeight(),
            GL.GL_RGBA,
            GL.GL_UNSIGNED_BYTE,
            imageImpl.getData());
      }

      @Override
      public void removeFromTexture(@Nonnull Image image, int x, int y, int w, int h) {
        // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
        // texture atlas when individual textures are removed. If necessary this can be enabled with a system property.
        if (!fillRemovedTexture) {
          return;
        }
        ByteBuffer initialData = Buffers.newDirectByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int i = 0; i < image.getWidth() * image.getHeight(); i++) {
          initialData.put((byte) 0xff);
          initialData.put((byte) 0x00);
          initialData.put((byte) 0x00);
          initialData.put((byte) 0xff);
        }
        initialData.rewind();

        final GL2 gl = GLContext.getCurrentGL().getGL2();
        bind();
        gl.glTexSubImage2D(
            GL2.GL_TEXTURE_2D,
            0,
            x,
            y,
            w,
            h,
            GL2.GL_RGBA,
            GL2.GL_UNSIGNED_BYTE,
            initialData);
      }
    };

    if (atlas) {
      atlasTexture = texture;
    }

    return texture;
  }

  @Nullable
  @Override
  public BatchRendererTexture.Image loadImage(@Nonnull final String filename) {
    ImageData loader = createImageLoader(filename);
    InputStream imageStream = null;
    try {
      imageStream = resourceLoader.getResourceAsStream(filename);
      if (imageStream != null) {
        ByteBuffer image = loader.loadImageDirect(imageStream);
        image.rewind();
        int width = loader.getWidth();
        int height = loader.getHeight();
        return new ImageImpl(image, width, height);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "problems loading image [" + filename + "]", e);
    } finally {
      if (imageStream != null) {
        try {
          imageStream.close();
        } catch (IOException ignored) {
        }
      }
    }
    return null;
  }

  @Nullable
  @Override
  public BatchRendererTexture.Image loadImage(@Nonnull final ByteBuffer data, final int w, final int h) {
    return new ImageImpl(data, w, h);
  }

  @Override
  public void beginBatch(@Nonnull final BatchRendererTexture texture, @Nonnull final BlendMode blendMode) {
    List<Batch> batchList;

    if (texture == atlasTexture) {
      batchList = atlasBatches;
    } else {
      batchList = batches.get(texture);
      if (batchList == null) {
        batchList = new ArrayList<Batch>();
        batches.put(texture, batchList);
      }
    }

    Batch batch = batchPool.allocate();
    batchList.add(batch);
    batch.begin(blendMode);

    currentBatches.put(texture, batch);
  }

  @Override
  public void addQuad(
      @Nonnull final BatchRendererTexture texture,
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
    Batch currentBatch = currentBatches.get(texture);

    if (!currentBatch.canAddQuad()) {
      beginBatch(texture, currentBatch.getBlendMode());
    }
    currentBatch.addQuadInternal(x, y, width, height, color1, color2, color3, color4, textureX, textureY,
        textureWidth, textureHeight);
  }

  @Override
  public int render() {
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glEnable(GL.GL_BLEND);
    gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
    gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    for (Map.Entry<BatchRendererTexture, List<Batch>> entry: batches.entrySet()) {
      entry.getKey().bind();
      for (Batch batch : entry.getValue()) {
        batch.render();
      }
    }

    gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
    gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    gl.glDisable(GL2.GL_BLEND);
    gl.glDisable(GL2.GL_TEXTURE_2D);

    return batches.size();
  }

  private void getViewport() {
    final GL gl = GLContext.getCurrentGL();
    gl.glGetIntegerv(GL2.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Viewport: " + viewportWidth + ", " + viewportHeight);
    }
  }

  // internal implementations

  private void checkGLError() {
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    int error = gl.glGetError();
    if (error != GL.GL_NO_ERROR) {
      String glerrmsg = glu.gluErrorString(error);
      log.warning("Error: (" + error + ") " + glerrmsg);
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Nonnull
  private Cursor loadMouseCursor(
      @Nonnull final String name,
      final int hotspotX,
      final int hotspotY) throws IOException {
    ImageData imageLoader = createImageLoader(name);
    InputStream in = resourceLoader.getResourceAsStream(name);
    if (in == null) {
      throw new IOException("Can't find resource to load a cursor.");
    }
    try {
      BufferedImage image = imageLoader.loadMouseCursorImage(in);
      return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(hotspotX, hotspotY), name);
    } finally {
      try {
        in.close();
      } catch (IOException ignored) {
      }
    }
  }

  @Nonnull
  private ImageData createImageLoader(@Nonnull final String name) {
    if (name.endsWith(".tga")) {
      return new TGAImageData();
    }
    return new ImageIOImageData();
  }

  private int createAtlasTexture(
      @Nonnull final ByteBuffer data,
      final int width,
      final int height,
      final boolean filter,
      final int srcPixelFormat) throws Exception {
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    int textureId = createTextureId();
    int minFilter = GL.GL_NEAREST;
    int magFilter = GL.GL_NEAREST;
    if (filter) {
      minFilter = GL2.GL_LINEAR_MIPMAP_LINEAR;
      magFilter = GL2.GL_NEAREST;
    }
    bind(textureId);

    IntBuffer temp = Buffers.newDirectIntBuffer(16);
    gl.glGetIntegerv(GL2.GL_MAX_TEXTURE_SIZE, temp);
    checkGLError();

    int max = temp.get(0);
    if ((width > max) || (height > max)) {
      throw new Exception("Attempt to allocate a texture to big for the current hardware");
    }
    if (width < 0) {
      throw new Exception("Attempt to allocate a texture with negative width");
    }
    if (height < 0) {
      throw new Exception("Attempt to allocate a texture with negative height");
    }

    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, minFilter);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, magFilter);
    checkGLError();

    data.rewind();
    gl.glTexImage2D(
        GL2.GL_TEXTURE_2D,
        0,
        4,
        width,
        height,
        0,
        srcPixelFormat,
        GL2.GL_UNSIGNED_BYTE,
        data);
    checkGLError();

    return textureId;
  }

  private void bind(int textureId) {
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
    checkGLError();
  }

  private int createTextureId() {
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    IntBuffer tmp = createIntBuffer(1);
    gl.glGenTextures(1, tmp);
    checkGLError();
    return tmp.get(0);
  }

  @Nonnull
  private IntBuffer createIntBuffer(final int size) {
    ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
    temp.order(ByteOrder.nativeOrder());
    return temp.asIntBuffer();
  }

  /**
   * Simple BatchRenderBackend.Image implementation that will transport the dimensions of an image as well as the
   * actual bytes from the loadImage() to the addImageToTexture() method.
   *
   * @author void
   */
  private static class ImageImpl implements BatchRendererTexture.Image {
    private final ByteBuffer buffer;
    private final int width;
    private final int height;

    private ImageImpl(final ByteBuffer buffer, final int width, final int height) {
      this.buffer = buffer;
      this.width = width;
      this.height = height;
    }

    @Override
    public int getWidth() {
      return width;
    }

    @Override
    public int getHeight() {
      return height;
    }

    @Override
    public ByteBuffer getData() {
      return buffer;
    }
  }

  /**
   * This class helps us to manage the batch data. We'll keep a bunch of instances of this class around that will be
   * reused when needed. Each Batch instance provides room for a certain amount of vertices and we'll use a new Batch
   * when we exceed this amount of data.
   *
   * @author void
   */
  private static class Batch {
    // 4 vertices per quad and 8 vertex attributes per vertex:
    // - 2 x pos
    // - 2 x texture
    // - 4 x color
    private final static int PRIMITIVE_SIZE = 4 * 8;
    private final static int SIZE = 64 * 1024; // 64k
    private final FloatBuffer vertexBuffer;

    private int primitiveCount;
    @Nonnull
    private final float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
    private BlendMode blendMode = BlendMode.BLEND;

    private Batch() {
      vertexBuffer = Buffers.newDirectFloatBuffer(SIZE);
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

      final GL2 gl = GLContext.getCurrentGL().getGL2();
      if (blendMode.equals(BlendMode.BLEND)) {
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      } else if (blendMode.equals(BlendMode.MULIPLY)) {
        gl.glBlendFunc(GL2.GL_DST_COLOR, GL2.GL_ZERO);
      }

      vertexBuffer.flip();
      vertexBuffer.position(0);
      gl.glVertexPointer(2, GL.GL_FLOAT, 8 * 4, vertexBuffer);

      vertexBuffer.position(2);
      gl.glColorPointer(4, GL.GL_FLOAT, 8 * 4, vertexBuffer);

      vertexBuffer.position(6);
      gl.glTexCoordPointer(2, GL.GL_FLOAT, 8 * 4, vertexBuffer);

      gl.glDrawArrays(GL2.GL_QUADS, 0, primitiveCount * 4);
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

      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color1.getRed();
      primitiveBuffer[bufferIndex++] = color1.getGreen();
      primitiveBuffer[bufferIndex++] = color1.getBlue();
      primitiveBuffer[bufferIndex++] = color1.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex++] = textureY;

      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color2.getRed();
      primitiveBuffer[bufferIndex++] = color2.getGreen();
      primitiveBuffer[bufferIndex++] = color2.getBlue();
      primitiveBuffer[bufferIndex++] = color2.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex++] = textureY;

      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color4.getRed();
      primitiveBuffer[bufferIndex++] = color4.getGreen();
      primitiveBuffer[bufferIndex++] = color4.getBlue();
      primitiveBuffer[bufferIndex++] = color4.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;

      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color3.getRed();
      primitiveBuffer[bufferIndex++] = color3.getGreen();
      primitiveBuffer[bufferIndex++] = color3.getBlue();
      primitiveBuffer[bufferIndex++] = color3.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex] = textureY + textureHeight;

      vertexBuffer.put(primitiveBuffer);
      primitiveCount++;
    }
  }
}
