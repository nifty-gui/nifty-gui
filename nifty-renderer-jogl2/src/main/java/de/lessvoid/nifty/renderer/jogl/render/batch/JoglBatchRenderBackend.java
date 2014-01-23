package de.lessvoid.nifty.renderer.jogl.render.batch;

import de.lessvoid.nifty.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} implementation includes full support for multiple
 * texture atlases and non-atlas textures.
 *
 * Jogl-specific implementation of the {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} interface. This
 * implementation will be the most backwards-compatible because it doesn't use any functions beyond OpenGL 1.1. It is
 * suitable for desktop devices.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatchRenderBackend implements BatchRenderBackend {
  @Nonnull
  private final BatchRenderBackend internalBackend;

  JoglBatchRenderBackend(@Nonnull final BatchRenderBackendInternal internalBackend) {
    this.internalBackend = internalBackend;
  }

  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader resourceLoader) {
    internalBackend.setResourceLoader(resourceLoader);
  }

  @Override
  public int getWidth() {
    return internalBackend.getWidth();
  }

  @Override
  public int getHeight() {
    return internalBackend.getHeight();
  }

  @Override
  public void beginFrame() {
    internalBackend.beginFrame();
  }

  @Override
  public void endFrame() {
    internalBackend.endFrame();
  }

  @Override
  public void clear() {
    internalBackend.clear();
  }

  @Nullable
  @Override
  public MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException {
    return internalBackend.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(@Nonnull MouseCursor mouseCursor) {
    internalBackend.enableMouseCursor(mouseCursor);
  }

  @Override
  public void disableMouseCursor() {
    internalBackend.disableMouseCursor();
  }

  @Override
  public int createTextureAtlas(int atlasWidth, int atlasHeight) {
    return internalBackend.createTextureAtlas(atlasWidth, atlasHeight);
  }

  @Override
  public void clearTextureAtlas(int atlasTextureId) {
    internalBackend.clearTextureAtlas(atlasTextureId);
  }

  @Nonnull
  @Override
  public Image loadImage(@Nonnull String filename) {
    return internalBackend.loadImage(filename);
  }

  @Nullable
  @Override
  public Image loadImage(@Nonnull ByteBuffer imageData, int imageWidth, int imageHeight) {
    return internalBackend.loadImage(imageData, imageWidth, imageHeight);
  }

  @Override
  public void addImageToAtlas(Image image, int atlasX, int atlasY, int atlasTextureId) {
    internalBackend.addImageToAtlas(image, atlasX, atlasY, atlasTextureId);
  }

  @Override
  public int createNonAtlasTexture(@Nonnull Image image) {
    return internalBackend.createNonAtlasTexture(image);
  }

  @Override
  public void deleteNonAtlasTexture(int textureId) {
    internalBackend.deleteNonAtlasTexture(textureId);
  }

  @Override
  public boolean existsNonAtlasTexture(int textureId) {
    return internalBackend.existsNonAtlasTexture(textureId);
  }

  @Override
  public void addQuad(
          float x,
          float y,
          float width,
          float height,
          @Nonnull Color color1,
          @Nonnull Color color2,
          @Nonnull Color color3,
          @Nonnull Color color4,
          float textureX,
          float textureY,
          float textureWidth,
          float textureHeight,
          int textureId) {
    internalBackend.addQuad(
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
            textureHeight,
            textureId);
  }

  @Override
  public void beginBatch(@Nonnull BlendMode blendMode, int textureId) {
    internalBackend.beginBatch(blendMode, textureId);
  }

  @Override
  public int render() {
    return internalBackend.render();
  }

  @Override
  public void removeImageFromAtlas(
          @Nonnull Image image,
          int atlasX,
          int atlasY,
          int imageWidth,
          int imageHeight,
          int atlasTextureId) {
    internalBackend.removeImageFromAtlas(image, atlasX, atlasY, imageWidth, imageHeight, atlasTextureId);
  }

  @Override
  public void useHighQualityTextures(boolean shouldUseHighQualityTextures) {
    internalBackend.useHighQualityTextures(shouldUseHighQualityTextures);
  }

  @Override
  public void fillRemovedImagesInAtlas(boolean shouldFill) {
    internalBackend.fillRemovedImagesInAtlas(shouldFill);
  }
}
