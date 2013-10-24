package de.lessvoid.nifty.renderer.jogl.render.batch;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.batch.Batch;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.renderer.jogl.math.Mat4;
import de.lessvoid.nifty.renderer.jogl.render.JoglImage;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursor;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.*;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D.ColorFormat;
import de.lessvoid.nifty.renderer.jogl.render.batch.core.CoreTexture2D.ResizeFilter;
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
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 * BatchRenderBackend Implementation for OpenGL Core Profile.
 *
 * TODO This class does not support multiple texture atlases or non-atlas textures yet.
 *
 * @author void
 */
public class JoglBatchRenderBackendCoreProfile implements BatchRenderBackend <JoglBatchCoreProfile> {
  @Nonnull
  private static final Logger log = Logger.getLogger(JoglBatchRenderBackendCoreProfile.class.getName());
  @Nonnull
  private static final IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4 * 4);
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private CoreTexture2D texture;
  @Nonnull
  private final CoreShader niftyShader;
  private Mat4 modelViewProjection;
  @Nonnull
  private final ObjectPool<JoglBatchCoreProfile> batchPool;
  private JoglBatchCoreProfile currentBatch;
  @Nonnull
  private final List<JoglBatchCoreProfile> batches = new ArrayList<JoglBatchCoreProfile>();
  private ByteBuffer initialData;
  @Nullable
  private MouseCursor mouseCursor;
  private boolean shouldUseHighQualityTextures = false;
  private boolean shouldFillRemovedImagesInAtlas = false;

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

    batchPool = new ObjectPool<JoglBatchCoreProfile>(new Factory<JoglBatchCoreProfile>() {
      @Nonnull
      @Override
      public JoglBatchCoreProfile createNew() {
        return createBatch();
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

    for (JoglBatchCoreProfile batch : batches) {
      batchPool.free(batch);
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
  public MouseCursor createMouseCursor(
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    return new JoglMouseCursor(filename, hotspotX, hotspotY, resourceLoader);
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

      initialData = Buffers.newDirectByteBuffer(atlasWidth * atlasHeight * 4);
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

  @Nullable
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
        return new JoglImage(image, width, height);
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
    return new JoglImage(null, 0, 0);
  }

  @Nullable
  @Override
  public Image loadImage(@Nonnull final ByteBuffer data, final int w, final int h) {
    return new ByteBufferedImage(data, w, h);
  }

  @Override
  public void addImageToAtlas(@Nonnull final Image image, final int x, final int y, final int atlasTextureId) {
    JoglImage joglImage = (JoglImage) image;
    if (joglImage.getWidth() == 0 ||
        joglImage.getHeight() == 0) {
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
        joglImage.getBuffer());
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
  public void beginBatch(@Nonnull final BlendMode blendMode, final int textureId) {
    batches.add(batchPool.allocate());
    currentBatch = batches.get(batches.size() - 1);
    currentBatch.begin(blendMode, textureId);
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
    currentBatch.addQuad(x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth,
            textureHeight);
  }

  @Nonnull
  @Override
  public JoglBatchCoreProfile createBatch() {
    return new JoglBatchCoreProfile(niftyShader);
  }

  @Override
  public int render() {
    niftyShader.activate();
    bind();

    final GL gl = GLContext.getCurrentGL();
    gl.glEnable(GL.GL_BLEND);

    for (Batch batch : batches) {
      batch.render();
    }

    gl.glDisable(GL2.GL_BLEND);
    return batches.size();
  }

  @Override
  public void removeImageFromAtlas(@Nonnull final Image image, final int atlasX, final int atlasY, final int imageWidth, final int imageHeight, final int atlasTextureId) {
    // Since we clear the whole texture when we switch screens it's not really necessary to remove data from the
    // texture atlas when individual textures are removed. If necessary this can be enabled with a system property.
    if (!shouldFillRemovedImagesInAtlas) {
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

    final GL gl = GLContext.getCurrentGL();
    bind();
    gl.glTexSubImage2D(
        GL.GL_TEXTURE_2D,
        0,
        atlasX,
        atlasY,
        imageWidth,
        imageHeight,
        GL.GL_RGBA,
        GL.GL_UNSIGNED_BYTE,
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
    final GL gl = GLContext.getCurrentGL();
    gl.glGetIntegerv(GL.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Viewport: " + viewportWidth + ", " + viewportHeight);
    }
  }

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

  private void createAtlasTexture(final int width, final int height) throws Exception {
    ByteBuffer initialData = Buffers.newDirectByteBuffer(width*height*4);
    for (int i=0; i<width*height*4; i++) {
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
