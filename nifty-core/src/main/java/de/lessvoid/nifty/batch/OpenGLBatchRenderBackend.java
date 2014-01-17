package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.Factory;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract base class for OpenGL batch rendering that gives OpenGL (& OpenGL ES) - based
 * {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} implementations some default functionality to avoid having to
 * reinvent the wheel and to prevent unnecessary code duplication. Fully OpenGL ES compatible - this class doesn't
 * require the implementation of any OpenGL methods that are not available in OpenGL ES.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public abstract class OpenGLBatchRenderBackend <T extends OpenGlBatch> implements BatchRenderBackend <T> {
  @Nonnull
  private static final Logger log = Logger.getLogger(OpenGLBatchRenderBackend.class.getName());
  private static final int INVALID_TEXTURE_ID = -1;
  @Nonnull
  private final IntBuffer viewportBuffer = createNativeOrderedIntBuffer(16);
  @Nonnull
  private final IntBuffer textureSizeBuffer = createNativeOrderedIntBuffer(16);
  @Nonnull
  private final IntBuffer singleTextureIdBuffer = createNativeOrderedIntBuffer(1);
  private int viewportWidth;
  private int viewportHeight;
  @Nonnull
  private ObjectPool<T> batchPool;
  private T currentBatch;
  @Nonnull
  private final List<T> batches = new ArrayList<T>();
  @Nonnull
  private ArrayList<Integer> nonAtlasTextureIds = new ArrayList<Integer>();
  @Nonnull
  private Map<Integer, Integer> atlasWidths = new HashMap<Integer, Integer>();
  @Nonnull
  private Map<Integer, Integer> atlasHeights = new HashMap<Integer, Integer>();
  private boolean shouldUseHighQualityTextures = false;
  private boolean shouldFillRemovedImagesInAtlas = false;
  private NiftyResourceLoader resourceLoader;
  @Nullable
  private MouseCursor mouseCursor;

  public OpenGLBatchRenderBackend() {
    initializeBatchPool();
    initializeOpenGL();
  }

  /**
   * You must return a non-null instance of your {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image}
   * implementation, even if buffer is null.
   */
  @Nonnull
  protected abstract Image createImageFromBuffer(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight);

  /**
   * OpenGL requires images to be in the format of a {@link java.nio.ByteBuffer}.
   */
  @Nullable
  protected abstract ByteBuffer getImageAsBuffer(final Image image);

  /**
   * {@link java.nio.ByteBuffer} factory method.
   */
  @Nonnull
  protected abstract ByteBuffer createNativeOrderedByteBuffer(final int numBytes);

  /**
   * {@link java.nio.IntBuffer} factory method.
   */
  @Nonnull
  protected abstract IntBuffer createNativeOrderedIntBuffer(final int numInts);

  // OpenGL constants
  protected abstract int GL_ALPHA_TEST();
  protected abstract int GL_BLEND();
  protected abstract int GL_COLOR_ARRAY();
  protected abstract int GL_COLOR_BUFFER_BIT();
  protected abstract int GL_CULL_FACE();
  protected abstract int GL_DEPTH_TEST();
  protected abstract int GL_INVALID_ENUM();
  protected abstract int GL_INVALID_OPERATION();
  protected abstract int GL_INVALID_VALUE();
  protected abstract int GL_LIGHTING();
  protected abstract int GL_LINEAR();
  protected abstract int GL_MAX_TEXTURE_SIZE();
  protected abstract int GL_MODELVIEW();
  protected abstract int GL_NEAREST();
  protected abstract int GL_NO_ERROR();
  protected abstract int GL_NOTEQUAL();
  protected abstract int GL_OUT_OF_MEMORY();
  protected abstract int GL_PROJECTION();
  protected abstract int GL_RGBA();
  protected abstract int GL_STACK_OVERFLOW();
  protected abstract int GL_STACK_UNDERFLOW();
  protected abstract int GL_TEXTURE_2D();
  protected abstract int GL_TEXTURE_COORD_ARRAY();
  protected abstract int GL_TEXTURE_MAG_FILTER();
  protected abstract int GL_TEXTURE_MIN_FILTER();
  protected abstract int GL_UNSIGNED_BYTE();
  protected abstract int GL_VERTEX_ARRAY();
  protected abstract int GL_VIEWPORT();

  // OpenGL methods
  protected abstract void glAlphaFunc (int func, float ref);
  protected abstract void glBindTexture (int target, int texture);
  protected abstract void glClear (int mask);
  protected abstract void glClearColor (float red, float green, float blue, float alpha);
  protected abstract void glDeleteTextures (int n, IntBuffer textures);
  protected abstract void glDisable (int cap);
  protected abstract void glDisableClientState (int array);
  protected abstract void glEnable (int cap);
  protected abstract void glEnableClientState (int array);
  protected abstract void glGenTextures (int n, IntBuffer textures);
  protected abstract int glGetError();
  protected abstract void glGetIntegerv (int pname, IntBuffer params);
  protected abstract void glLoadIdentity();
  protected abstract void glMatrixMode (int mode);
  protected abstract void glOrthof (float left, float right, float bottom, float top, float zNear, float zFar);
  protected abstract void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels);
  protected abstract void glTexParameterf (int target, int pname, float param);
  protected abstract void glTexSubImage2D (int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels);
  protected abstract void glTranslatef (float x, float y, float z);
  protected abstract void glViewport (int x, int y, int width, int height);

  @Override
  public void enableMouseCursor(@Nonnull MouseCursor mouseCursor) {
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      mouseCursor.disable();
    }
  }

  @Nonnull
  protected Image createImageFromFile(@Nullable String filename) {
    if (filename == null) {
      return createImageFromBuffer(null, 0, 0);
    }
    ImageLoader loader = ImageLoaderFactory.createImageLoader(filename);
    InputStream imageStream = null;
    try {
      imageStream = getResourceLoader().getResourceAsStream(filename);
      if (imageStream != null) {
        ByteBuffer image = loader.loadAsByteBufferRGBA(imageStream);
        image.rewind();
        int width = loader.getImageWidth();
        int height = loader.getImageHeight();
        return createImageFromBuffer(image, width, height);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not load image from file: [" + filename + "]", e);
    } finally {
      if (imageStream != null) {
        try {
          imageStream.close();
        } catch (IOException ignored) {
        }
      }
    }
    return createImageFromBuffer(null, 0, 0);
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    log.fine("setResourceLoader()");
    this.resourceLoader = resourceLoader;
  }

  @Nonnull
  protected NiftyResourceLoader getResourceLoader() {
    return resourceLoader;
  }

  @Override
  public int getWidth() {
    log.fine("getWidth()");
    updateViewport();
    return viewportWidth;
  }

  @Override
  public int getHeight() {
    log.fine("getHeight()");
    updateViewport();
    return viewportHeight;
  }

  @Override
  public void beginFrame() {
    log.fine("beginFrame()");
    deleteBatches();
  }

  @Override
  public void endFrame() {
    log.fine("endFrame()");
    checkGLError();
  }

  @Override
  public void clear() {
    log.fine("clear()");
    clearGlColorBufferWithBlack();
  }

  @Override
  public int createTextureAtlas(final int atlasWidth, final int atlasHeight) {
    log.fine("createTextureAtlas()");
    try {
      return createAtlasTextureInternal(atlasWidth, atlasHeight);
    } catch (Exception e) {
      textureCreationFailed(atlasWidth, atlasHeight, e);
      return INVALID_TEXTURE_ID;
    }
  }

  @Override
  public void clearTextureAtlas(final int atlasTextureId) {
    log.fine("clearTextureAtlas()");
    bindGlTexture(atlasTextureId);
    updateCurrentlyBoundGlTexture(createBlankImageDataForAtlas(atlasTextureId), getAtlasWidth(atlasTextureId), getAtlasHeight(atlasTextureId));
  }

  @Nonnull
  @Override
  public Image loadImage(@Nonnull final String filename) {
    log.fine("loadImage()");
    return createImageFromFile(filename);
  }

  @Nullable
  @Override
  public Image loadImage(@Nonnull final ByteBuffer data, final int width, final int height) {
    log.fine("loadImage2()");
    return createImageFromBuffer(data, width, height);
  }

  @Override
  public void addImageToAtlas(
          @Nonnull final Image image,
          final int atlasX,
          final int atlasY,
          final int atlasTextureId) {
    log.fine("addImageToAtlas()");
    bindGlTexture(atlasTextureId);
    updateCurrentlyBoundGlTexture(getImageAsBuffer(image), atlasX, atlasY, image.getWidth(), image.getHeight());
  }

  @Override
  public int createNonAtlasTexture(@Nullable final Image image) {
    log.fine("createNonAtlasTexture()");
    if (image == null) {
      return INVALID_TEXTURE_ID;
    }
    try {
      return createNonAtlasTextureInternal(getImageAsBuffer(image), image.getWidth(), image.getHeight());
    } catch (Exception e) {
      textureCreationFailed(getImageWidth(image), getImageHeight(image), e);
      return INVALID_TEXTURE_ID;
    }
  }

  @Override
  public void deleteNonAtlasTexture(final int textureId) {
    log.fine("deleteNonAtlasTexture()");
    try {
      deleteNonAtlasTextureInternal(textureId);
    } catch (Exception e) {
      textureDeletionFailed(textureId, e);
    }
  }

  @Override
  public boolean existsNonAtlasTexture(final int textureId) {
    log.fine("existsNonAtlasTexture()");
    return nonAtlasTextureIds.contains(textureId);
  }

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
          final float textureHeight,
          final int textureId) {
    log.fine("addQuad()");
    updateCurrentBatch(textureId);
    addQuadToCurrentBatch(
            x,
            y,
            width,
            height,
            color1,
            color2,
            color3,
            color4,
            textureX,
            textureY,
            textureWidth,
            textureHeight);
  }

  @Override
  public void beginBatch(@Nonnull final BlendMode blendMode, final int textureId) {
    log.fine("beginBatch()");
    currentBatch = getNewBatch();
    addBatch(currentBatch);
    currentBatch.begin(blendMode, textureId);
  }

  @Override
  public int render() {
    log.fine("render()");
    beginRendering();
    renderBatches();
    endRendering();
    return getTotalBatchesRendered();
  }

  @Override
  public void removeImageFromAtlas(
          @Nonnull final Image image,
          final int atlasX,
          final int atlasY,
          final int imageWidth,
          final int imageHeight,
          final int atlasTextureId) {
    log.fine("removeImageFromAtlas()");
    // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
    // texture atlas when individual textures are removed. If necessary this can be enabled with a system property.
    if (shouldFillRemovedImagesInAtlas) {
      bindGlTexture(atlasTextureId);
      updateCurrentlyBoundGlTexture(createBlankImageData(imageWidth, imageHeight), atlasX, atlasY, imageWidth, imageHeight);
    }
  }

  @Override
  public void useHighQualityTextures(final boolean shouldUseHighQualityTextures) {
    log.info(shouldUseHighQualityTextures ? "Using high quality textures (near & far bilinear filtering, " +
            "mipmapping not supported in this implementation." : "Using low quality textures (no filtering).");
    this.shouldUseHighQualityTextures = shouldUseHighQualityTextures;
  }

  @Override
  public void fillRemovedImagesInAtlas(final boolean shouldFill) {
    shouldFillRemovedImagesInAtlas = shouldFill;
  }

  // Internal implementations

  private void initializeBatchPool() {
    batchPool = new ObjectPool<T>(new Factory<T>() {
      @Nonnull
      @Override
      public T createNew() {
        return createBatch();
      }
    });
  }

  private void initializeOpenGL() {
    glViewport(0, 0, getWidth(), getHeight());
    glMatrixMode(GL_PROJECTION());
    glLoadIdentity();
    glOrthof(0.0f, (float) getWidth(), (float) getHeight(), 0.0f, -9999.0f, 9999.0f);
    glMatrixMode(GL_MODELVIEW());
    glLoadIdentity();
    glDisable(GL_DEPTH_TEST());
    glDisable(GL_CULL_FACE());
    glDisable(GL_LIGHTING());
    glEnable(GL_ALPHA_TEST());
    glEnable(GL_BLEND());
    glEnable(GL_TEXTURE_2D());
    glAlphaFunc(GL_NOTEQUAL(), 0);
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT());
    // Enable exact pixelization for 2D rendering
    // See: http://www.opengl.org/archives/resources/faq/technical/transformations.htm#tran0030
    glTranslatef(0.375f, 0.375f, 0.0f);
  }

  private void updateViewport() {
    viewportBuffer.clear();
    glGetIntegerv(GL_VIEWPORT(), viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    log.fine("Updated viewport: width: " + viewportWidth + ", height: " + viewportHeight);
  }

  private ByteBuffer createBlankImageData(final int textureWidth, final int textureHeight) {
    return createNativeOrderedByteBuffer(textureWidth * textureHeight * 4);
  }

  private ByteBuffer createBlankImageDataForAtlas(final int atlasTextureId) {
    return createBlankImageData(getAtlasWidth(atlasTextureId), getAtlasHeight(atlasTextureId));
  }

  private int getAtlasWidth(final int atlasTextureId) {
    return atlasWidths.get(atlasTextureId);
  }

  private int getAtlasHeight(final int atlasTextureId) {
    return atlasHeights.get(atlasTextureId);
  }

  private void deleteBatches() {
    for (T batch : batches) {
      batchPool.free(batch);
    }
    batches.clear();
  }

  private void clearGlColorBufferWithBlack() {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT());
  }

  private int createAtlasTextureInternal(final int width, final int height)
          throws Exception {
    int atlasTextureId = createGlTexture(createBlankImageData(width, height), width, height);
    saveAtlasSize(atlasTextureId, width, height);
    return atlasTextureId;
  }

  private void textureCreationFailed(
          final int textureWidth,
          final int textureHeight,
          final Exception exception) {
    log.log(Level.WARNING, "Failed to create texture of width: " + textureWidth + " & height: " + textureHeight +
            ".", exception);
  }

  private int getImageWidth(final Image image) {
    return image != null ? image.getWidth() : 0;
  }

  private int getImageHeight(final Image image) {
    return image != null ? image.getHeight() : 0;
  }

  private int createGlTexture(final ByteBuffer imageData, final int textureWidth, final int textureHeight) throws Exception {
    checkGlTextureSize(textureWidth, textureHeight);
    int glTextureId = createTextureId();
    bindGlTexture(glTextureId);
    updateCurrentlyBoundGlTexture(imageData, textureWidth, textureHeight);
    setCurrentlyBoundGlTextureFilteringQuality(shouldUseHighQualityTextures);
    return glTextureId;
  }

  private void bindGlTexture(final int textureId) {
    glBindTexture(GL_TEXTURE_2D(), textureId);
  }

  private void updateCurrentlyBoundGlTexture(final ByteBuffer imageData, final int width, final int height) {
    glTexImage2D(
            GL_TEXTURE_2D(),
            0,
            GL_RGBA(),
            width,
            height,
            0,
            GL_RGBA(),
            GL_UNSIGNED_BYTE(),
            imageData);
    checkGLError();
  }

  private void updateCurrentlyBoundGlTexture(
          final ByteBuffer imageData,
          final int subTextureX,
          final int subTextureY,
          final int subTextureWidth,
          final int subTextureHeight) {
    glTexSubImage2D(
            GL_TEXTURE_2D(),
            0,
            subTextureX,
            subTextureY,
            subTextureWidth,
            subTextureHeight,
            GL_RGBA(),
            GL_UNSIGNED_BYTE(),
            imageData);
    checkGLError();
  }

  private void setCurrentlyBoundGlTextureFilteringQuality(final boolean isHighQuality) {
    if (isHighQuality) {
      glTexParameterf(GL_TEXTURE_2D(), GL_TEXTURE_MIN_FILTER(), GL_LINEAR());
      glTexParameterf(GL_TEXTURE_2D(), GL_TEXTURE_MAG_FILTER(), GL_LINEAR());
    } else {
      glTexParameterf(GL_TEXTURE_2D(), GL_TEXTURE_MIN_FILTER(), GL_NEAREST());
      glTexParameterf(GL_TEXTURE_2D(), GL_TEXTURE_MAG_FILTER(), GL_NEAREST());
    }
    checkGLError();
  }

  private void saveAtlasSize(final int atlasTextureId, final int atlasWidth, final int atlasHeight) {
    atlasWidths.put(atlasTextureId, atlasWidth);
    atlasHeights.put(atlasTextureId, atlasHeight);
  }

  private int createTextureId() {
    singleTextureIdBuffer.clear();
    glGenTextures(1, singleTextureIdBuffer);
    checkGLError();
    return singleTextureIdBuffer.get(0);
  }

  private void checkGLError() {
    int error = glGetError();

    if (error == GL_NO_ERROR()) {
      return;
    }

    String errorMessage = getGlErrorMessage(error);

    log.warning("Error: (" + error + ") " + errorMessage);

    try {
      throw new Exception();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getGlErrorMessage(final int error) {
    if (error == GL_INVALID_ENUM()) {
      return "Invalid enum";
    } else if (error == GL_INVALID_VALUE()) {
      return "Invalid value";
    } else if (error == GL_INVALID_OPERATION()) {
      return "Invalid operation";
    } else if (error == GL_STACK_OVERFLOW()) {
      return "Stack overflow";
    } else if (error == GL_STACK_UNDERFLOW()) {
      return "Stack underflow";
    } else if (error == GL_OUT_OF_MEMORY()) {
      return "Out of memory";
    } else {
      return "";
    }
  }

  private void checkGlTextureSize(final int textureWidth, final int textureHeight) throws Exception {
    int maxSize = getMaxGlTextureSize();
    if (textureWidth > maxSize || textureHeight > maxSize) {
      throw new Exception("Attempted to create a texture size beyond the capability of your GPU.\nAttempted " +
              "size: " + textureWidth + " x " + textureHeight + ".\nMaximum size: " + maxSize + " x " + maxSize +
              ".");
    }
    if (textureWidth < 0) {
      throw new Exception("Attempted to create a texture with negative width of: " + textureWidth + ".");
    }
    if (textureHeight < 0) {
      throw new Exception("Attempted to create a texture with negative height of: " + textureHeight + ".");
    }
  }

  private int getMaxGlTextureSize() {
    textureSizeBuffer.clear();
    glGetIntegerv(GL_MAX_TEXTURE_SIZE(), textureSizeBuffer);
    checkGLError();
    return textureSizeBuffer.get(0);
  }

  private int createNonAtlasTextureInternal(final ByteBuffer image, final int width, final int height) throws Exception {
    int textureId = createGlTexture(image, width, height);
    nonAtlasTextureIds.add(textureId);
    return textureId;
  }

  private void deleteNonAtlasTextureInternal(final int textureId) {
    singleTextureIdBuffer.clear();
    singleTextureIdBuffer.put(0, textureId);
    glDeleteTextures(1, singleTextureIdBuffer);
    checkGLError();
    nonAtlasTextureIds.remove(textureId);
  }

  private void textureDeletionFailed(final int textureId, final Exception exception) {
    log.log(Level.WARNING, "Failed to delete texture width id: " + textureId + ".", exception);
  }

  private void addQuadToCurrentBatch(
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
    currentBatch.addQuad(
            x,
            y,
            width,
            height,
            color1,
            color2,
            color3,
            color4,
            textureX,
            textureY,
            textureWidth,
            textureHeight);
  }

  private void updateCurrentBatch(final int textureId) {
    if (shouldBeginBatch()) {
      beginBatch(getCurrentBlendMode(), textureId);
    }
  }

  private BlendMode getCurrentBlendMode() {
    return currentBatch.getBlendMode();
  }

  private boolean shouldBeginBatch() {
    return !currentBatch.canAddQuad();
  }

  private T getNewBatch() {
    return batchPool.allocate();
  }

  private void addBatch (final T batch) {
    batches.add(batch);
  }

  private void renderBatches() {
    for (T batch : batches) {
      batch.render();
    }
  }

  private void beginRendering() {
    glEnable(GL_TEXTURE_2D());
    glEnable(GL_BLEND());
    glEnableClientState(GL_VERTEX_ARRAY());
    glEnableClientState(GL_COLOR_ARRAY());
    glEnableClientState(GL_TEXTURE_COORD_ARRAY());
  }

  private void endRendering() {
    glDisableClientState(GL_TEXTURE_COORD_ARRAY());
    glDisableClientState(GL_COLOR_ARRAY());
    glDisableClientState(GL_VERTEX_ARRAY());
    glDisable(GL_BLEND());
    glDisable(GL_TEXTURE_2D());
  }

  private int getTotalBatchesRendered() {
    return batches.size();
  }
}
