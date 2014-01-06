package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRendererTexture;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglMouseCursor;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.*;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreTexture2D.ResizeFilter;
import de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData;
import de.lessvoid.nifty.renderer.lwjgl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.lwjgl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.Factory;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART;
import static org.lwjgl.opengl.GL31.glPrimitiveRestartIndex;


/**
 * BatchRenderBackend Implementation for OpenGL Core Profile.
 *
 * @author void
 */
public class LwjglBatchRenderBackendCoreProfile implements BatchRenderBackend {
  private static final Logger log = Logger.getLogger(LwjglBatchRenderBackendCoreProfile.class.getName());
  private static final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  @Nonnull
  private final CoreShader niftyShader;
  @Nonnull
  private final ObjectPool<Batch> batchPool;
  private BatchRendererTexture atlasTexture;
  private final List<Batch> atlasBatches = new ArrayList<Batch>();
  private final Map<BatchRendererTexture, List<Batch>> batches = new HashMap<BatchRendererTexture, List<Batch>>();
  private final Map<BatchRendererTexture, Batch> currentBatches = new HashMap<BatchRendererTexture, Batch>();

  private static final int PRIMITIVE_SIZE = 4 * 8; // 4 vertices per quad and 12 vertex attributes per vertex (2xpos,
  // 2xtexture, 4xcolor, 4xclipping)
  private static final int PRIMITIVE_RESTART_INDEX = 0xFFFF;

  @Nonnull
  private float[] primitiveBuffer = new float[PRIMITIVE_SIZE];
  @Nonnull
  private int[] elementIndexBuffer = new int[5];
  private final CoreProfileSaveGLState saveGLState = new CoreProfileSaveGLState();

  public LwjglBatchRenderBackendCoreProfile() {
    niftyShader = CoreShader.newShaderWithVertexAttributes("aVertex", "aColor", "aTexture");
    niftyShader.fragmentShader("nifty.fs");
    niftyShader.vertexShader("nifty.vs");
    niftyShader.link();
    niftyShader.activate();
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
    saveGLState.saveCore();

    Matrix4f modelViewProjection = CoreMatrixFactory.createOrtho(0, getWidth(), getHeight(), 0);
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
    saveGLState.restoreCore();
    checkGLError();
  }

  @Override
  public void clear() {
    log.fine("clear()");

    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  @Nonnull
  @Override
  public MouseCursor createMouseCursor(
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    return new LwjglMouseCursor(loadMouseCursor(filename, hotspotX, hotspotY));
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    Cursor nativeCursor = ((LwjglMouseCursor) mouseCursor).getCursor();
    try {
      Mouse.setNativeCursor(nativeCursor);
    } catch (LWJGLException e) {
      log.warning(e.getMessage());
    }
  }

  @Override
  public void disableMouseCursor() {
    try {
      Mouse.setNativeCursor(null);
    } catch (LWJGLException e) {
      log.warning(e.getMessage());
    }
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
    try {
      ByteBuffer image = loader.loadImageDirect(resourceLoader.getResourceAsStream(filename));
      image.rewind();
      int width = loader.getWidth();
      int height = loader.getHeight();
      return new ImageImpl(image, width, height);
    } catch (Exception e) {
      log.log(Level.WARNING, "problems loading image [" + filename + "]", e);
      return new ImageImpl(null, 0, 0);
    }
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
    glActiveTexture(GL_TEXTURE0);
    glEnable(GL11.GL_BLEND);
    glEnable(GL_PRIMITIVE_RESTART);
    glPrimitiveRestartIndex(PRIMITIVE_RESTART_INDEX);

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

    glDisable(GL_PRIMITIVE_RESTART);
    glDisable(GL11.GL_BLEND);

    return batchesCount;
  }

  private void getViewport() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Viewport: " + viewportWidth + ", " + viewportHeight);
    }
  }

  // internal implementations

  private void checkGLError() {
    int error = GL11.glGetError();
    if (error != GL11.GL_NO_ERROR) {
      String glerrmsg = GLU.gluErrorString(error);
      log.warning("Error: (" + error + ") " + glerrmsg);
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Nullable
  private Cursor loadMouseCursor(
      @Nonnull final String name,
      final int hotspotX,
      final int hotspotY) throws IOException {
    ImageData imageLoader = createImageLoader(name);
    InputStream source = null;
    try {
      source = resourceLoader.getResourceAsStream(name);
      ByteBuffer imageData = imageLoader.loadMouseCursorImage(source);
      imageData.rewind();
      int width = imageLoader.getWidth();
      int height = imageLoader.getHeight();
      return new Cursor(width, height, hotspotX, height - hotspotY - 1, 1, imageData.asIntBuffer(), null);
    } catch (LWJGLException e) {
      throw new IOException(e);
    } finally {
      if (source != null) {
        source.close();
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
    private final ByteBuffer buffer;
    private final int width;
    private final int height;

    private ImageImpl(ByteBuffer buffer, int width, int height) {
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
  private class Batch {
    // 4 vertices per quad and 8 vertex attributes per vertex:
    // - 2 x pos
    // - 2 x texture
    // - 4 x color
    private final static int PRIMITIVE_SIZE = 4 * 8;
    private static final int SIZE = 64 * 1024; // 64k

    @Nonnull
    private BlendMode blendMode = BlendMode.BLEND;

    private int primitiveCount;
    @Nonnull
    private final CoreVAO vao;
    @Nonnull
    private final CoreVBO vbo;
    @Nonnull
    private final CoreElementVBO elementVbo;
    private int globalIndex;
    private int indexCount;

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
      indexCount = 0;
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
      indexCount = 0;
      vao.unbind();
    }

    @Nonnull
    public BlendMode getBlendMode() {
      return blendMode;
    }

    public void render() {
      if (primitiveCount == 0) return; // Attempting to render with an empty vertex buffer crashes the program.

      if (blendMode.equals(BlendMode.BLEND)) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      } else if (blendMode.equals(BlendMode.MULIPLY)) {
        GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
      }

      vao.bind();
      vbo.getBuffer().flip();
      vbo.bind();
      vbo.send();
      elementVbo.getBuffer().flip();
      elementVbo.bind();
      elementVbo.send();
      CoreRender.renderTriangleStripIndexed(indexCount);
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
      int elementIndexBufferIndex = 0;

      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color3.getRed();
      primitiveBuffer[bufferIndex++] = color3.getGreen();
      primitiveBuffer[bufferIndex++] = color3.getBlue();
      primitiveBuffer[bufferIndex++] = color3.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y + height;
      primitiveBuffer[bufferIndex++] = color4.getRed();
      primitiveBuffer[bufferIndex++] = color4.getGreen();
      primitiveBuffer[bufferIndex++] = color4.getBlue();
      primitiveBuffer[bufferIndex++] = color4.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex++] = textureY + textureHeight;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

      primitiveBuffer[bufferIndex++] = x;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color1.getRed();
      primitiveBuffer[bufferIndex++] = color1.getGreen();
      primitiveBuffer[bufferIndex++] = color1.getBlue();
      primitiveBuffer[bufferIndex++] = color1.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX;
      primitiveBuffer[bufferIndex++] = textureY;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;

      primitiveBuffer[bufferIndex++] = x + width;
      primitiveBuffer[bufferIndex++] = y;
      primitiveBuffer[bufferIndex++] = color2.getRed();
      primitiveBuffer[bufferIndex++] = color2.getGreen();
      primitiveBuffer[bufferIndex++] = color2.getBlue();
      primitiveBuffer[bufferIndex++] = color2.getAlpha();
      primitiveBuffer[bufferIndex++] = textureX + textureWidth;
      primitiveBuffer[bufferIndex] = textureY;
      elementIndexBuffer[elementIndexBufferIndex++] = globalIndex++;
      elementIndexBuffer[elementIndexBufferIndex] = PRIMITIVE_RESTART_INDEX;

      indexCount += 5;

      vbo.getBuffer().put(primitiveBuffer);
      elementVbo.getBuffer().put(elementIndexBuffer);
      primitiveCount++;
    }
  }
}
