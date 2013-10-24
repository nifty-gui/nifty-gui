package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglImage;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglMouseCursor;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreMatrixFactory;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreShader;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreTexture2D;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.core.CoreTexture2D.ResizeFilter;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART;
import static org.lwjgl.opengl.GL31.glPrimitiveRestartIndex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

/**
 * BatchRenderBackend Implementation for OpenGL Core Profile.
 *
 * TODO This class does not support multiple texture atlases or non-atlas textures yet.
 *
 * @author void
 */
public class LwjglBatchRenderBackendCoreProfile implements BatchRenderBackend <LwjglBatchCoreProfile> {
  private static final Logger log = Logger.getLogger(LwjglBatchRenderBackendCoreProfile.class.getName());
  private static final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
  private static final int PRIMITIVE_RESTART_INDEX = 0xFFFF;
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private CoreTexture2D texture;
  @Nonnull
  private final CoreShader niftyShader;
  @Nonnull
  private final ObjectPool<LwjglBatchCoreProfile> batchPool;
  private LwjglBatchCoreProfile currentBatch;
  private final List<LwjglBatchCoreProfile> batches = new ArrayList<LwjglBatchCoreProfile>();
  private ByteBuffer initialData;
  @Nonnull
  private final CoreProfileSaveGLState saveGLState = new CoreProfileSaveGLState();
  @Nullable
  private MouseCursor mouseCursor;
  private boolean shouldUseHighQualityTextures = false;
  private boolean shouldFillRemovedImagesInAtlas = false;

  public LwjglBatchRenderBackendCoreProfile() {
    niftyShader = CoreShader.newShaderWithVertexAttributes("aVertex", "aColor", "aTexture");
    niftyShader.fragmentShader("nifty.fs");
    niftyShader.vertexShader("nifty.vs");
    niftyShader.link();
    niftyShader.activate();
    niftyShader.setUniformi("uTex", 0);

    batchPool = new ObjectPool<LwjglBatchCoreProfile>(new Factory<LwjglBatchCoreProfile>() {
      @Nonnull
      @Override
      public LwjglBatchCoreProfile createNew() {
        return new LwjglBatchCoreProfile(niftyShader, PRIMITIVE_RESTART_INDEX);
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

    for (LwjglBatchCoreProfile batch : batches) {
      batchPool.free(batch);
    }
    batches.clear();
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
    return new LwjglMouseCursor(filename, hotspotX, hotspotY, resourceLoader);
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      mouseCursor.disable();
    }
  }

  @Override
  public int createTextureAtlas(final int atlasWidth, final int atlasHeight) {
    try {
      createAtlasTexture(atlasWidth, atlasHeight);

      initialData = BufferUtils.createByteBuffer(atlasWidth * atlasHeight * 4);
      for (int i = 0; i < atlasWidth * atlasHeight; i++) {
        initialData.put((byte) 0x00);
        initialData.put((byte) 0xff);
        initialData.put((byte) 0x00);
        initialData.put((byte) 0xff);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, e.getMessage(), e);
    }
    return -1;
  }

  @Override
  public void clearTextureAtlas(final int atlasTextureId) {
    initialData.rewind();
    bind();
    texture.updateTextureData(initialData);
  }

  @Nonnull
  @Override
  public Image loadImage(@Nonnull final String filename) {
    ImageLoader loader = ImageLoaderFactory.createImageLoader(filename);
    InputStream imageStream = null;
    try {
      imageStream = resourceLoader.getResourceAsStream(filename);
      if (imageStream != null) {
        ByteBuffer image = loader.loadImageDirect(imageStream);
        image.rewind();
        int width = loader.getWidth();
        int height = loader.getHeight();
        return new LwjglImage(image, width, height);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not load image from file: [" + filename + "]", e);
    } finally {
      try {
        if (imageStream != null) {
          imageStream.close();
        }
      } catch (IOException e) {
        log.log(Level.INFO, "An error occurred while closing the InputStream used to load image: " + "[" + filename +
                "].", e);
      }
    }
    return new LwjglImage(null, 0, 0);
  }

  @Nullable
  @Override
  public Image loadImage(@Nonnull final ByteBuffer data, final int w, final int h) {
    return new LwjglImage(data, w, h);
  }

  @Override
  public void addImageToAtlas(@Nonnull final Image image, final int atlasX, final int atlasY, final int atlasTextureId) {
    LwjglImage lwjglImage = (LwjglImage) image;
    if (lwjglImage.getWidth() == 0 || lwjglImage.getHeight() == 0) {
      return;
    }
    bind();
    GL11.glTexSubImage2D(
        GL11.GL_TEXTURE_2D,
        0,
        atlasX,
        atlasY,
        image.getWidth(),
        image.getHeight(),
        GL11.GL_RGBA,
        GL11.GL_UNSIGNED_BYTE,
        lwjglImage.getBuffer());
  }

  @Override
  public int createNonAtlasTexture(@Nonnull final Image image) {
    // TODO implement method
    return -1;
  }

  @Override
  public void deleteNonAtlasTexture(final int textureId) {
    // TODO implement method
  }

  @Override
  public boolean existsNonAtlasTexture(final int textureId) {
    // TODO implement method
    return false;
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
    if (!currentBatch.canAddQuad()) {
      beginBatch(currentBatch.getBlendMode(), textureId);
    }
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

  @Nonnull
  @Override
  public LwjglBatchCoreProfile createBatch() {
    return new LwjglBatchCoreProfile(niftyShader, PRIMITIVE_RESTART_INDEX);
  }

  @Override
  public void beginBatch(@Nonnull final BlendMode blendMode, final int textureId) {
    batches.add(batchPool.allocate());
    currentBatch = batches.get(batches.size() - 1);
    currentBatch.begin(blendMode, textureId);
  }

  @Override
  public int render() {
    glActiveTexture(GL_TEXTURE0);
    bind();
    glEnable(GL11.GL_BLEND);
    glEnable(GL_PRIMITIVE_RESTART);
    glPrimitiveRestartIndex(PRIMITIVE_RESTART_INDEX);

    for (LwjglBatchCoreProfile batch : batches) {
      batch.render();
    }

    glDisable(GL_PRIMITIVE_RESTART);
    glDisable(GL11.GL_BLEND);

    return batches.size();
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
    // texture atlas when individual textures are removed. If necessary this can be enabled with a system property.
    if (!shouldFillRemovedImagesInAtlas) {
      return;
    }
    ByteBuffer initialData = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
    for (int i = 0; i < image.getWidth() * image.getHeight(); i++) {
      initialData.put((byte) 0xff);
      initialData.put((byte) 0x00);
      initialData.put((byte) 0x00);
      initialData.put((byte) 0xff);
    }
    initialData.rewind();

    bind();
    GL11.glTexSubImage2D(
        GL11.GL_TEXTURE_2D,
        0,
        atlasX,
        atlasY,
        imageWidth,
        imageHeight,
        GL11.GL_RGBA,
        GL11.GL_UNSIGNED_BYTE,
        initialData);
  }

  @Override
  public void useHighQualityTextures(boolean shouldUseHighQualityTextures) {
    log.info(shouldUseHighQualityTextures ? "Using high quality textures (near & far bilinear filtering, " +
            "mipmapping is supported, but not yet implemented in this implementation." : "Using low quality " +
            "textures (no filtering).");
    this.shouldUseHighQualityTextures = shouldUseHighQualityTextures;
  }

  @Override
  public void fillRemovedImagesInAtlas(boolean shouldFill) {
    shouldFillRemovedImagesInAtlas = shouldFill;
  }

  // Internal implementations

  private void getViewport() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Viewport: " + viewportWidth + ", " + viewportHeight);
    }
  }

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

  private void createAtlasTexture(final int width, final int height) throws Exception {
    ByteBuffer initialData = BufferUtils.createByteBuffer(width * height * 4);
    for (int i = 0; i < width * height * 4; i++) {
      initialData.put((byte) 0x80);
    }
    initialData.rewind();
    texture = new CoreTexture2D(ColorFormat.RGBA, width, height, initialData, getTextureQuality());
  }

  private ResizeFilter getTextureQuality() {
    return shouldUseHighQualityTextures ? ResizeFilter.Linear : ResizeFilter.Nearest;
  }

  private void bind() {
    texture.bind();
  }
}
