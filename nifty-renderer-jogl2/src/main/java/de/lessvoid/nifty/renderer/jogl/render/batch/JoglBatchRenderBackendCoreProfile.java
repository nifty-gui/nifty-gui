package de.lessvoid.nifty.renderer.jogl.render.batch;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.jogl.math.Mat4;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursor;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreElementVBO;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreMatrixFactory;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreRender;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreShader;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D.ResizeFilter;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreVAO;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreVBO;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.ObjectPool.Factory;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * BatchRenderBackend Implementation for OpenGL Core Profile.
 * @author void
 */
public class JoglBatchRenderBackendCoreProfile implements BatchRenderBackend {
  private static Logger log = Logger.getLogger(JoglBatchRenderBackendCoreProfile.class.getName());
  private static IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4*4);
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private CoreTexture2D texture;
  private final CoreShader niftyShader;
  private Mat4 modelViewProjection;
  private final ObjectPool<Batch> batchPool;
  private Batch currentBatch;
  private final List<Batch> batches = new ArrayList<Batch>();
  private ByteBuffer initialData;
  private boolean fillRemovedTexture =
      Boolean.parseBoolean(System.getProperty(JoglBatchRenderBackendCoreProfile.class.getName() + ".fillRemovedTexture", "false"));

  private static final int PRIMITIVE_SIZE = 4*8; // 4 vertices per quad and 8 vertex attributes per vertex (2xpos, 2xtexture, 4xcolor)

  private float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  private int[] elementIndexBuffer = new int[6];

  public JoglBatchRenderBackendCoreProfile() {
    modelViewProjection = CoreMatrixFactory.createOrtho(0, getWidth(), getHeight(), 0);

    niftyShader = CoreShader.newShaderWithVertexAttributes("aVertex", "aColor", "aTexture");

    final GL gl = GLContext.getCurrentGL();
    if (gl.isGLES2()) {
      niftyShader.fragmentShader("nifty-es2.fs");
      niftyShader.vertexShader("nifty-es2.vs");
    } else {
      niftyShader.fragmentShader("nifty-gl3.fs");
      niftyShader.vertexShader("nifty-gl3.vs");
    }
    niftyShader.link();
    niftyShader.activate();
    niftyShader.setUniformMatrix4f("uModelViewProjectionMatrix", modelViewProjection);
    niftyShader.setUniformi("uTex", 0);

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
    getViewport();
    return viewportWidth;
  }

  @Override
  public int getHeight() {
    getViewport();
    return viewportHeight;
  }

  @Override
  public void beginFrame() {
    log.fine("beginFrame()");

    modelViewProjection = CoreMatrixFactory.createOrtho(0, getWidth(), getHeight(), 0);
    niftyShader.activate();
    niftyShader.setUniformMatrix4f("uModelViewProjectionMatrix", modelViewProjection);

    for (int i=0; i<batches.size(); i++) {
      batchPool.free(batches.get(i));
    }
    batches.clear();
  }

  @Override
  public void endFrame() {
    log.fine("endFrame");
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
  }

  @Override
  public void disableMouseCursor() {
  }

  @Override
  public void createAtlasTexture(final int width, final int height) {
    try {
      createAtlasTexture(width, height, GL.GL_RGBA);

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
    texture.updateTextureData(initialData);
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
    niftyShader.activate();
    bind();

    for (int i=0; i<batches.size(); i++) {
      Batch batch = batches.get(i);
      batch.render();
    }

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

    final GL gl = GLContext.getCurrentGL();
    gl.glTexSubImage2D(
        GL.GL_TEXTURE_2D,
        0,
        x,
        y,
        w,
        h,
        GL.GL_RGBA, 
        GL.GL_UNSIGNED_BYTE,
        initialData);

  }

  private void getViewport() {
    final GL gl = GLContext.getCurrentGL();
    gl.glGetIntegerv(GL.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Viewport: " + viewportWidth + ", " + viewportHeight);
    }
  }

  // internal implementations

  private void checkGLError() {
    final GL gl = GLContext.getCurrentGL();
    int error= gl.glGetError();
    if (error != GL.GL_NO_ERROR) {
      log.warning("Error: (" + error + ") ");
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

  private void createAtlasTexture(final int width, final int height, final int srcPixelFormat) throws Exception {
    ByteBuffer initialData = Buffers.newDirectByteBuffer(width*height*4);
    for (int i=0; i<width*height*4; i++) {
      initialData.put((byte) 0x80);
    }
    initialData.rewind();
    texture = new CoreTexture2D(ColorFormat.RGBA, width, height, initialData, ResizeFilter.Nearest);
  }

  private void bind() {
    texture.bind();
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

    private BlendMode blendMode = BlendMode.BLEND;

    private int primitiveCount;
    private final CoreVAO vao;
    private final CoreVBO vbo;
    private final CoreElementVBO elementVbo;
    private int globalIndex;
    private int triangleVertexCount;

    private Batch() {
      vao = new CoreVAO();
      vao.bind();

      elementVbo = CoreElementVBO.createStream(new int[SIZE]);
      elementVbo.bind();

      vbo = CoreVBO.createStream(new float[SIZE]);
      vbo.bind();

      vao.enableVertexAttributef(niftyShader.getAttribLocation("aVertex"), 2, 8, 0);
      vao.enableVertexAttributef(niftyShader.getAttribLocation("aColor"), 4, 8, 2);
      vao.enableVertexAttributef(niftyShader.getAttribLocation("aTexture"), 2, 8, 6);

      primitiveCount = 0;
      globalIndex = 0;
      triangleVertexCount = 0;
      vao.unbind();
    }

    public void begin(final BlendMode blendMode) {
      vao.bind();
      vbo.bind();
      vbo.getBuffer().clear();
      elementVbo.bind();
      elementVbo.getBuffer().clear();
      primitiveCount = 0;
      globalIndex = 0;
      triangleVertexCount = 0;
      vao.unbind();
    }

    public BlendMode getBlendMode() {
      return blendMode;
    }

    public void render() {
      final GL gl = GLContext.getCurrentGL();
      if (blendMode.equals(BlendMode.BLEND)) {
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
      } else if (blendMode.equals(BlendMode.MULIPLY)) {
        gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
      }

      vao.bind();
      vbo.getBuffer().flip();
      vbo.bind();
      vbo.send();
      elementVbo.getBuffer().flip();
      elementVbo.bind();
      elementVbo.send();
      CoreRender.renderTrianglesIndexed(triangleVertexCount);
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

      int elementIndexBufferIndex = 0;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 0;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 1;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 2;
      
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 2;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 3;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 0;

      triangleVertexCount += 6;
      globalIndex += 4;

      vbo.getBuffer().put(primitiveBuffer);
      elementVbo.getBuffer().put(elementIndexBuffer);
      primitiveCount++;
    }
  }
}
