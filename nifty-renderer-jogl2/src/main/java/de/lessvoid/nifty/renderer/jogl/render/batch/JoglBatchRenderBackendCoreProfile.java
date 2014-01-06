package de.lessvoid.nifty.renderer.jogl.render.batch;

import com.jogamp.common.nio.Buffers;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRendererTexture;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.jogl.math.Mat4;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursor;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.*;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D.ResizeFilter;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BatchRenderBackend Implementation for OpenGL Core Profile.
 *
 * @author void
 */
public class JoglBatchRenderBackendCoreProfile implements BatchRenderBackend {
  private static final Logger log = Logger.getLogger(JoglBatchRenderBackendCoreProfile.class.getName());
  private static final IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4 * 4);
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  @Nonnull
  private final CoreShader niftyShader;
  private Mat4 modelViewProjection;
  @Nonnull
  private final ObjectPool<Batch> batchPool;
  private final Map<BatchRendererTexture, List<Batch>> batches = new HashMap<BatchRendererTexture, List<Batch>>();
  private final Map<BatchRendererTexture, Batch> currentBatches = new HashMap<BatchRendererTexture, Batch>();
  private BatchRendererTexture atlasTexture;
  private final List<Batch> atlasBatches = new ArrayList<Batch>();

  private static final int PRIMITIVE_SIZE = 4 * 8; // 4 vertices per quad and 8 vertex attributes per vertex (2xpos,
  // 2xtexture, 4xcolor)


  @Nonnull
  private final float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  @Nonnull
  private final int[] elementIndexBuffer = new int[6];

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
  }

  @Override
  public void disableMouseCursor() {
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
    if (data.capacity() < width * height * 4) {
      log.severe("Atlas texture' buffer capacity is less than requested WIDTH x HEIGHT");
      return null;
    }
    try {
      data.rewind();
      BatchRendererTexture texture = new CoreTexture2D(ColorFormat.RGBA, width, height, data, ResizeFilter.Nearest);
      if (atlas) {
        atlasTexture = texture;
      }
      return texture;
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
    return null;
  }

  @Nullable
  @Override
  public BatchRendererTexture.Image loadImage(@Nonnull final String filename) {
    ImageData loader = createImageLoader(filename);
    InputStream in = resourceLoader.getResourceAsStream(filename);
    if (in != null) {
      try {
        ByteBuffer image = loader.loadImageDirect(in);
        image.rewind();
        int width = loader.getWidth();
        int height = loader.getHeight();
        return new ImageImpl(image, width, height);
      } catch (Exception e) {
        log.log(Level.WARNING, "problems loading image [" + filename + "]", e);
      } finally {
        try {
          in.close();
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
    niftyShader.activate();
    final GL gl = GLContext.getCurrentGL();
    gl.glEnable(GL.GL_BLEND);

    int batchesCount = 0;

    atlasTexture.bind();
    for (Batch batch : atlasBatches) {
      batch.render();
      batchesCount++;
    }

    for (Map.Entry<BatchRendererTexture, List<Batch>> entry: batches.entrySet()) {
      entry.getKey().bind();
      for (Batch batch : entry.getValue()) {
        batch.render();
        batchesCount++;
      }
    }

    gl.glDisable(GL2.GL_BLEND);
    return batchesCount;
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
    int error = gl.glGetError();
    if (error != GL.GL_NO_ERROR) {
      log.warning("Error: (" + error + ") ");
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

  /**
   * Simple BatchRenderBackend.Image implementation that will transport the dimensions of an image as well as the
   * actual bytes from the loadImage() to the addImageToTexture() method.
   *
   * @author void
   */
  public static class ImageImpl implements BatchRendererTexture.Image {
    private final ByteBuffer data;
    private final int width;
    private final int height;

    private ImageImpl(ByteBuffer buffer, int width, int height) {
      this.data = buffer;
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
      return data;
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
    private final static int PRIMITIVE_SIZE = 4 * 8;
    private final static int SIZE = 64 * 1024; // 64k

    @Nonnull
    private final BlendMode blendMode = BlendMode.BLEND;

    private int primitiveCount;
    @Nonnull
    private final CoreVAO vao;
    @Nonnull
    private final CoreVBO vbo;
    @Nonnull
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

    @Nonnull
    public BlendMode getBlendMode() {
      return blendMode;
    }

    public void render() {
      if (primitiveCount == 0) return; // Attempting to render with an empty vertex buffer crashes the program.

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

      int elementIndexBufferIndex = 0;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 1;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 2;

      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 2;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex + 3;
      elementIndexBuffer[elementIndexBufferIndex] = globalIndex;

      triangleVertexCount += 6;
      globalIndex += 4;

      vbo.getBuffer().put(primitiveBuffer);
      elementVbo.getBuffer().put(elementIndexBuffer);
      primitiveCount++;
    }
  }
}
