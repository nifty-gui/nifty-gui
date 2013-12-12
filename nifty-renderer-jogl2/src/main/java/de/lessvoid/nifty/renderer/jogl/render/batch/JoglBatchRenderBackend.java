package de.lessvoid.nifty.renderer.jogl.render.batch;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursor;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.ObjectPool.Factory;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Jogl BatchRenderBackend Implementation.
 * @author void
 */
public class JoglBatchRenderBackend implements BatchRenderBackend {
  private static Logger log = Logger.getLogger(JoglBatchRenderBackend.class.getName());
  private static IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4 * 4);
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private int textureId;
  private final ObjectPool<Batch> batchPool;
  private Batch currentBatch;
  private final List<Batch> batches = new ArrayList<Batch>();
  private ByteBuffer initialData;
  private boolean fillRemovedTexture =
      Boolean.parseBoolean(System.getProperty(JoglBatchRenderBackend.class.getName() + ".fillRemovedTexture", "false"));
  private final GLU glu;

  public JoglBatchRenderBackend() {
    glu = GLU.createGLU();
    batchPool = new ObjectPool<Batch>(2, new Factory<Batch>() {
      @Override
      public Batch createNew() {
        return new Batch();
      }
    });
  }

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
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

    for (int i=0; i<batches.size(); i++) {
      batchPool.free(batches.get(i));
    }
    batches.clear();
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
  public MouseCursor createMouseCursor(final String filename, final int hotspotX, final int hotspotY) throws IOException {
    return new JoglMouseCursor(loadMouseCursor(filename, hotspotX, hotspotY));
  }

  @Override
  public void enableMouseCursor(final MouseCursor mouseCursor) {
    // TODO implement this method later
  }

  @Override
  public void disableMouseCursor() {
    // TODO implement this method later
  }

  @Override
  public void createAtlasTexture(final int width, final int height) {
    try {
      createAtlasTexture(width, height, false, GL.GL_RGBA);

      initialData = Buffers.newDirectByteBuffer(width*height*4);
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

  @Override
  public void clearAtlasTexture(final int width, final int height) {
    initialData.rewind();
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
          initialData);
    checkGLError();
  }

  @Override
  public Image loadImage(final String filename) {
    ImageData loader = createImageLoader(filename);
    try {
      ByteBuffer image = loader.loadImageDirect(resourceLoader.getResourceAsStream(filename));
      image.rewind();
      int width = loader.getWidth();
      int height = loader.getHeight();
      return new ImageImpl(width, height, image);
    } catch (Exception e) {
      log.log(Level.WARNING, "problems loading image [" + filename + "]", e);
      return new ImageImpl(0, 0, null);
    }
  }

  @Override
  public void addImageToTexture(final Image image, final int x, final int y) {
    ImageImpl imageImpl = (ImageImpl) image;
    if (imageImpl.getWidth() == 0 ||
        imageImpl.getHeight() == 0) {
      return;
    }
    final GL gl = GLContext.getCurrentGL();
    gl.glTexSubImage2D(
        GL.GL_TEXTURE_2D,
        0,
        x,
        y,
        image.getWidth(),
        image.getHeight(),
        GL.GL_RGBA, 
        GL.GL_UNSIGNED_BYTE,
        imageImpl.byteBuffer);
  }

  @Override
  public void beginBatch(final BlendMode blendMode) {
    batches.add(batchPool.allocate());
    currentBatch = batches.get(batches.size() - 1);
    currentBatch.begin(blendMode);
  }

  @Override
  public void addQuad(
      final float x,
      final float y,
      final float width,
      final float height,
      final Color color1,
      final Color color2,
      final Color color3,
      final Color color4,
      final float textureX,
      final float textureY,
      final float textureWidth,
      final float textureHeight) {
    if (!currentBatch.canAddQuad()) {
      beginBatch(currentBatch.getBlendMode());
    }
    currentBatch.addQuadInternal(x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth, textureHeight);
  }

  @Override
  public int render() {
    bind();
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glEnable(GL.GL_BLEND);
    gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
    gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    for (int i=0; i<batches.size(); i++) {
      Batch batch = batches.get(i);
      batch.render();
    }

    gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
    gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    gl.glDisable(GL2.GL_BLEND);
    gl.glDisable(GL2.GL_TEXTURE_2D);

    return batches.size();
  }

  @Override
  public void removeFromTexture(final Image image, final int x, final int y, final int w, final int h) {
    // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
    // texture atlas when individual textures are removed. If necessary this can be enabled with a system property.
    if (!fillRemovedTexture) {
      return;
    }
    ByteBuffer initialData = Buffers.newDirectByteBuffer(image.getWidth()*image.getHeight()*4);
    for (int i=0; i<image.getWidth()*image.getHeight(); i++) {
      initialData.put((byte) 0xff);
      initialData.put((byte) 0x00);
      initialData.put((byte) 0x00);
      initialData.put((byte) 0xff);
    }
    initialData.rewind();

    final GL2 gl = GLContext.getCurrentGL().getGL2();
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
    int error= gl.glGetError();
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

  private Cursor loadMouseCursor(final String name, final int hotspotX, final int hotspotY) throws IOException {
    ImageData imageLoader = createImageLoader(name);
    BufferedImage image = imageLoader.loadMouseCursorImage(resourceLoader.getResourceAsStream(name));       
    return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(hotspotX,hotspotY), name);
}

  private ImageData createImageLoader(final String name) {
    if (name.endsWith(".tga")) {
      return new TGAImageData();
    }
    return new ImageIOImageData();
  }

  private void createAtlasTexture(final int width, final int height, final boolean filter, final int srcPixelFormat) throws Exception {
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    textureId = createTextureId();
    int minFilter = GL.GL_NEAREST;
    int magFilter = GL.GL_NEAREST;
    if (filter) {
      minFilter = GL2.GL_LINEAR_MIPMAP_LINEAR;
      magFilter = GL2.GL_NEAREST;
    }
    bind();

    IntBuffer temp = Buffers.newDirectIntBuffer(16);
    gl.glGetIntegerv(GL2.GL_MAX_TEXTURE_SIZE, temp);
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

    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, minFilter); 
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, magFilter); 
    checkGLError();

    ByteBuffer initialData = Buffers.newDirectByteBuffer(width*height*4);
    for (int i=0; i<width*height*4; i++) {
      initialData.put((byte) 0x80);
    }
    initialData.rewind();
    gl.glTexImage2D(
          GL2.GL_TEXTURE_2D, 
          0,
          4, 
          width, 
          height, 
          0, 
          srcPixelFormat, 
          GL2.GL_UNSIGNED_BYTE, 
          initialData);
    checkGLError();
  }

  private void bind() {
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
  private static class ImageImpl implements BatchRenderBackend.Image {
    private final int width;
    private final int height;
    private final ByteBuffer byteBuffer;

    public ImageImpl(final int width, final int height, final ByteBuffer byteBuffer) {
      this.width = width;
      this.height = height;
      this.byteBuffer = byteBuffer;
    }

    @Override
    public int getWidth() {
      return width;
    }

    @Override
    public int getHeight() {
      return height;
    }
  }

  /**
   * This class helps us to manage the batch data. We'll keep a bunch of instances of this class around that will be
   * reused when needed. Each Batch instance provides room for a certain amount of vertices and we'll use a new Batch
   * when we exceed this amount of data.
   *
   * @author void
   */
  private class Batch {
    // 4 vertices per quad and 8 vertex attributes per vertex:
    // - 2 x pos
    // - 2 x texture
    // - 4 x color
    private final static int PRIMITIVE_SIZE = 4*8;
    private final int SIZE = 64*1024; // 64k
    private final FloatBuffer vertexBuffer;

    private int primitiveCount;
    private float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
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
      gl.glVertexPointer(2, GL.GL_FLOAT, 8*4, vertexBuffer);

      vertexBuffer.position(2);
      gl.glColorPointer(4, GL.GL_FLOAT, 8*4, vertexBuffer);

      vertexBuffer.position(6);
      gl.glTexCoordPointer(2, GL.GL_FLOAT, 8*4, vertexBuffer);

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
        final Color color1,
        final Color color2,
        final Color color3,
        final Color color4,
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
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;

      vertexBuffer.put(primitiveBuffer);
      primitiveCount++;
    }
  }
}
