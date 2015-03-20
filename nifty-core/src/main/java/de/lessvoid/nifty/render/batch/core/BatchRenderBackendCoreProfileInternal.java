package de.lessvoid.nifty.render.batch.core;

import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.render.batch.core.CoreTexture2D.ResizeFilter;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.ImageFactory;
import de.lessvoid.nifty.render.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;
import de.lessvoid.nifty.render.batch.spi.core.CoreMatrixFactory;
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
 * Internal {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend} implementation for OpenGL Core Profile batch
 * rendering that provides OpenGL-based {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend}
 * implementations some default functionality to avoid having to reinvent the wheel and to prevent unnecessary code
 * duplication. Suitable for desktop devices.
 *
 * Note: Requires OpenGL 3.2 or higher. Mobiles devices & OpenGL ES are not officially supported yet with this class.
 *
 * {@inheritDoc}
 *
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class BatchRenderBackendCoreProfileInternal implements BatchRenderBackend {
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchRenderBackendInternal.class.getName());
  private static final int PRIMITIVE_RESTART_INDEX = 0xFFFF;
  private static final int INVALID_TEXTURE_ID = -1;
  @Nonnull
  private final CoreGL gl;
  @Nonnull
  private final BufferFactory bufferFactory;
  @Nonnull
  private final ImageFactory imageFactory;
  @Nonnull
  private final MouseCursorFactory mouseCursorFactory;
  @Nonnull
  private final CoreShader shader;
  @Nonnull
  private final IntBuffer viewportBuffer;
  @Nonnull
  private final ObjectPool<CoreBatch> batchPool;
  @Nonnull
  private final CoreProfileSaveGLState saveGLState;
  @Nonnull
  private final List<CoreBatch> batches = new ArrayList<CoreBatch>();
  @Nonnull
  private final Map<Integer, CoreTexture2D> atlasTextures = new HashMap<Integer, CoreTexture2D>();
  @Nonnull
  private final Map<Integer, CoreTexture2D> nonAtlasTextures = new HashMap<Integer, CoreTexture2D>();
  @Nonnull
  private final Map<String, MouseCursor> cursorCache = new HashMap<String, MouseCursor>();
  @Nullable
  private MouseCursor mouseCursor;
  @Nullable
  private NiftyResourceLoader resourceLoader;
  @Nullable
  private CoreBatch currentBatch;
  private int viewportWidth;
  private int viewportHeight;
  private boolean shouldUseHighQualityTextures = false;
  private boolean shouldFillRemovedImagesInAtlas = false;

  public BatchRenderBackendCoreProfileInternal(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final ImageFactory imageFactory,
          @Nonnull final MouseCursorFactory mouseCursorFactory) {
    this.gl = gl;
    this.bufferFactory = bufferFactory;
    this.imageFactory = imageFactory;
    this.mouseCursorFactory = mouseCursorFactory;
    this.saveGLState = new CoreProfileSaveGLState(gl, bufferFactory);
    viewportBuffer = bufferFactory.createNativeOrderedIntBuffer(16);
    shader = CoreShader.createShaderWithVertexAttributes(gl, bufferFactory, "aVertex", "aColor", "aTexture");
    shader.fragmentShader("nifty.fs");
    shader.vertexShader("nifty.vs");
    shader.link();
    shader.activate();
    shader.setUniformi("uTex", 0);
    batchPool = new ObjectPool<CoreBatch>(new Factory<CoreBatch>() {
      @Nonnull
      @Override
      public CoreBatch createNew() {
        return new CoreBatchInternal(gl, shader, bufferFactory, PRIMITIVE_RESTART_INDEX);
      }
    });
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    log.fine("setResourceLoader()");
    this.resourceLoader = resourceLoader;
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
    saveGLState.saveCore();
    shader.activate();
    shader.setUniformMatrix4f("uModelViewProjectionMatrix", CoreMatrixFactory.createOrthoMatrix(0, getWidth(), getHeight(), 0));
    deleteBatches();
  }

  @Override
  public void endFrame() {
    log.fine("endFrame()");
    saveGLState.restoreCore();
    CheckGL.checkGLError(gl);
  }

  @Override
  public void clear() {
    log.fine("clear()");
    clearGlColorBufferWithBlack();
  }

  @Nullable
  @Override
  public MouseCursor createMouseCursor(@Nonnull final String filename, final int hotspotX, final int hotspotY)
          throws IOException {
    log.fine("createMouseCursor()");
    return existsCursor(filename) ? getCursor(filename) : createCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    log.fine("enableMouseCursor()");
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      log.fine("disableMouseCursor()");
      mouseCursor.disable();
    }
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
    updateAtlasTexture(atlasTextureId, createBlankImageDataForAtlas(atlasTextureId));
  }

  @Nonnull
  @Override
  public Image loadImage(@Nonnull final String filename) {
    log.fine("loadImage()");
    return createImageFromFile(filename);
  }

  @Nullable
  @Override
  public Image loadImage(@Nonnull final ByteBuffer imageData, final int imageWidth, final int imageHeight) {
    log.fine("loadImage2()");
    return imageFactory.create(imageData, imageWidth, imageHeight);
  }

  @Override
  public void addImageToAtlas(
          @Nonnull final Image image,
          final int atlasX,
          final int atlasY,
          final int atlasTextureId) {
    log.fine("addImageToAtlas()");
    log.fine("addImageToAtlas with atlas texture id: " + atlasTextureId);
    updateAtlasTextureSection(
            atlasTextureId,
            imageFactory.asByteBuffer(image),
            atlasX,
            atlasY,
            image.getWidth(),
            image.getHeight());
  }

  @Override
  public int createNonAtlasTexture(@Nonnull final Image image) {
    log.fine("createNonAtlasTexture()");
    try {
      if (imageFactory.asByteBuffer(image) == null) {
        log.severe("Attempted to create a non atlas texture with null image data!");
        return INVALID_TEXTURE_ID;
      }
      return createNonAtlasTextureInternal(imageFactory.asByteBuffer(image), image.getWidth(), image.getHeight());
    } catch (Exception e) {
      textureCreationFailed(image.getWidth(), image.getHeight(), e);
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
    return nonAtlasTextures.containsKey(textureId);
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
    currentBatch = createNewBatch();
    addBatch(currentBatch);
    currentBatch.begin(blendMode, findTexture(textureId));
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
    // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
    // texture atlas when individual textures are removed.
    if (! shouldFillRemovedImagesInAtlas) {
      return;
    }
    log.fine("removeImageFromAtlas()");
    updateAtlasTextureSection(
            atlasTextureId,
            createBlankImageData(imageWidth, imageHeight),
            atlasX,
            atlasY,
            imageWidth,
            imageHeight);
  }

  @Override
  public void useHighQualityTextures(final boolean shouldUseHighQualityTextures) {
    log.fine("useHighQualityTextures()");
    log.info(shouldUseHighQualityTextures ? "Using high quality textures (near & far trilinear filtering with " +
            "mipmaps)." : "Using low quality textures (no filtering).");
    this.shouldUseHighQualityTextures = shouldUseHighQualityTextures;
  }

  @Override
  public void fillRemovedImagesInAtlas(final boolean shouldFill) {
    log.fine("fillRemovedImagesInAtlas()");
    log.info(shouldFill ? "Filling in removed images in atlas." : "Not filling in removed images in atlas.");
    shouldFillRemovedImagesInAtlas = shouldFill;
  }

  // Internal implementations

  private void updateViewport() {
    viewportBuffer.clear();
    gl.glGetIntegerv(gl.GL_VIEWPORT(), viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    log.fine("Updated viewport: width: " + viewportWidth + ", height: " + viewportHeight);
  }

  @Nonnull
  private ByteBuffer createBlankImageDataForAtlas(final int atlasTextureId) {
    return createBlankImageData(getAtlasWidth(atlasTextureId), getAtlasHeight(atlasTextureId));
  }

  @Nonnull
  private ByteBuffer createBlankImageData(final int textureWidth, final int textureHeight) {
    return this.bufferFactory.createNativeOrderedByteBuffer(textureWidth * textureHeight * 4);
  }

  private int getAtlasWidth(final int atlasTextureId) {
    return getAtlasTexture(atlasTextureId).getWidth();
  }

  private int getAtlasHeight(final int atlasTextureId) {
    return getAtlasTexture(atlasTextureId).getHeight();
  }

  private void deleteBatches() {
    for (CoreBatch batch : batches) {
      batchPool.free(batch);
    }
    batches.clear();
  }

  private void clearGlColorBufferWithBlack() {
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(gl.GL_COLOR_BUFFER_BIT());
  }

  private int createAtlasTextureInternal(final int width, final int height) throws Exception {
    CoreTexture2D atlasTexture = createTexture(createBlankImageData(width, height), width, height);
    log.fine("createAtlasTextureInternal with atlas texture id: " + atlasTexture.getId());
    atlasTextures.put(atlasTexture.getId(), atlasTexture);
    return atlasTexture.getId();
  }

  private void textureCreationFailed(
          final int textureWidth,
          final int textureHeight,
          @Nonnull final Exception exception) {
    log.log(Level.SEVERE, "Failed to create texture of width: " + textureWidth + " & height: " + textureHeight +
            ".", exception);
  }

  @Nonnull
  private CoreTexture2D createTexture(
          @Nonnull final ByteBuffer imageData,
          final int textureWidth,
          final int textureHeight) throws Exception {
    return new CoreTexture2D(
            gl,
            bufferFactory,
            ColorFormat.RGBA,
            textureWidth,
            textureHeight,
            imageData,
            getTextureQuality());
  }

  @Nonnull
  private ResizeFilter getTextureQuality() {
    return shouldUseHighQualityTextures ? ResizeFilter.LinearMipMapLinear : ResizeFilter.Nearest;
  }

  private void updateAtlasTexture(final int atlasTextureId, @Nullable final ByteBuffer imageData) {
    bindAtlasTexture(atlasTextureId);
    getAtlasTexture(atlasTextureId).updateTextureData(imageData);
  }

  private void updateAtlasTextureSection(
          final int atlasTextureId,
          @Nullable final ByteBuffer imageData,
          final int atlasSectionX,
          final int atlasSectionY,
          final int atlasSectionWidth,
          final int atlasSectionHeight) {
    if (imageData == null) {
      log.severe("Attempted to update section of atlas texture (id: " + atlasTextureId + ") with null image data!");
      return;
    }
    log.fine("updateAtlasTextureSection with atlas texture id: " + atlasTextureId);
    bindAtlasTexture(atlasTextureId);
    // TODO Move this OpenGL call and error check to CoreTexture2D!
    gl.glTexSubImage2D(
            gl.GL_TEXTURE_2D(),
            0,
            atlasSectionX,
            atlasSectionY,
            atlasSectionWidth,
            atlasSectionHeight,
            gl.GL_RGBA(),
            gl.GL_UNSIGNED_BYTE(),
            imageData);
    CheckGL.checkGLError(gl, "Failed to update section [x, y, w, h]: [" + atlasSectionX + ", " + atlasSectionY + ", " +
            atlasSectionWidth + ", " + atlasSectionHeight + "] of atlas texture with id: " + atlasTextureId + ".");
  }

  private void bindAtlasTexture(final int atlasTextureId) {
    log.fine("bindAtlasTexture with atlas texture id: " + atlasTextureId);
    log.fine("bindAtlasTexture: available atlas texture ids:");
    for (int textureId : atlasTextures.keySet()) {
      log.fine("bindAtlasTexture: available atlas texture id: " + textureId);
    }
    getAtlasTexture(atlasTextureId).bind();
  }

  private CoreTexture2D getAtlasTexture(final int atlasTextureId) {
    return atlasTextures.get(atlasTextureId);
  }

  private int createNonAtlasTextureInternal(@Nonnull final ByteBuffer imageData, final int width, final int height)
          throws Exception {
    CoreTexture2D nonAtlasTexture = createTexture(imageData, width, height);
    nonAtlasTextures.put(nonAtlasTexture.getId(), nonAtlasTexture);
    return nonAtlasTexture.getId();
  }

  private void deleteNonAtlasTextureInternal(final int nonAtlasTextureId) {
    getNonAtlasTexture(nonAtlasTextureId).dispose();
    nonAtlasTextures.remove(nonAtlasTextureId);
  }

  private CoreTexture2D getNonAtlasTexture(final int nonAtlasTextureId) {
    return nonAtlasTextures.get(nonAtlasTextureId);
  }

  private void textureDeletionFailed(final int textureId, final Exception exception) {
    log.log(Level.WARNING, "Failed to delete texture width id: " + textureId + ".", exception);
  }

  private CoreTexture2D findTexture(final int textureId) {
    if (atlasTextures.containsKey(textureId)) {
      return atlasTextures.get(textureId);
    } else if (nonAtlasTextures.containsKey(textureId)) {
      return nonAtlasTextures.get(textureId);
    } else {
      throw new IllegalStateException("Cannot find texture with id: " + textureId);
    }
  }

  private void addQuadToCurrentBatch(
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
    assert currentBatch != null;
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

  @Nonnull
  private BlendMode getCurrentBlendMode() {
    assert currentBatch != null;
    return currentBatch.getBlendMode();
  }

  private boolean shouldBeginBatch() {
    assert currentBatch != null;
    return !currentBatch.canAddQuad();
  }

  @Nonnull
  private CoreBatch createNewBatch() {
    return batchPool.allocate();
  }

  private void addBatch (@Nonnull final CoreBatch batch) {
    batches.add(batch);
  }

  private void renderBatches() {
    for (CoreBatch batch : batches) {
      batch.render();
    }
  }

  private void beginRendering() {
    gl.glActiveTexture(gl.GL_TEXTURE0());
    gl.glBindSampler(0, 0); // make sure default tex unit and sampler are bound
    gl.glEnable(gl.GL_BLEND());
    gl.glEnable(gl.GL_PRIMITIVE_RESTART());
    gl.glPrimitiveRestartIndex(PRIMITIVE_RESTART_INDEX);
  }

  private void endRendering() {
    gl.glDisable(gl.GL_PRIMITIVE_RESTART());
    gl.glDisable(gl.GL_BLEND());
  }

  private int getTotalBatchesRendered() {
    return batches.size();
  }

  private boolean existsCursor(@Nonnull final String filename) {
    return cursorCache.containsKey(filename);
  }

  @Nonnull
  private MouseCursor getCursor(@Nonnull final String filename) {
    assert cursorCache.containsKey(filename);
    return cursorCache.get(filename);
  }

  @Nullable
  private MouseCursor createCursor (final String filename, final int hotspotX, final int hotspotY) {
    try {
      assert resourceLoader != null;
      cursorCache.put(filename, mouseCursorFactory.create(filename, hotspotX, hotspotY, resourceLoader));
      return cursorCache.get(filename);
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not create mouse cursor [" + filename + "]", e);
      return null;
    }
  }

  @Nonnull
  private Image createImageFromFile(@Nonnull final String filename) {
    ImageLoader loader = ImageLoaderFactory.createImageLoader(filename);
    InputStream imageStream = null;
    try {
      assert resourceLoader != null;
      imageStream = resourceLoader.getResourceAsStream(filename);
      if (imageStream != null) {
        ByteBuffer image = loader.loadAsByteBufferRGBA(imageStream);
        image.rewind();
        int width = loader.getImageWidth();
        int height = loader.getImageHeight();
        return imageFactory.create(image, width, height);
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
    return imageFactory.create(null, 0, 0);
  }
}
