package de.lessvoid.nifty.slick2d.render.batch;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.batch.BatchRenderBackendInternal;
import de.lessvoid.nifty.render.batch.spi.*;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class SlickBatchRenderBackend implements BatchRenderBackend {
  @Nonnull
  private static final Logger log = Logger.getLogger(SlickBatchRenderBackend.class.getName());
  @Nonnull
  private final BatchRenderBackend delegate;
  @Nonnull
  private final GameContainer gameContainer;
  @Nullable
  private MouseCursor mouseCursor;

  SlickBatchRenderBackend(
          @Nonnull final GL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final ImageFactory imageFactory,
          @Nonnull final MouseCursorFactory mouseCursorFactory,
          @Nonnull final GameContainer gameContainer) {
    this.gameContainer = gameContainer;
    delegate = new BatchRenderBackendInternal(gl, bufferFactory, imageFactory, mouseCursorFactory);
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    delegate.setResourceLoader(resourceLoader);
  }

  @Override
  public int getWidth() {
    return delegate.getWidth();
  }

  @Override
  public int getHeight() {
    return delegate.getHeight();
  }

  @Override
  public void beginFrame() {
    delegate.beginFrame();
  }

  @Override
  public void endFrame() {
    delegate.endFrame();
    if (mouseCursor instanceof SlickMouseCursor) {
      final Input input = gameContainer.getInput();
      ((SlickMouseCursor)mouseCursor).render(gameContainer.getGraphics(), input.getMouseX(), input.getMouseY());
    }
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Nullable
  @Override
  public MouseCursor createMouseCursor(@Nonnull final String filename, final int hotspotX, final int hotspotY)
          throws IOException {
    return delegate.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    delegate.enableMouseCursor(mouseCursor);
    this.mouseCursor = mouseCursor;
  }

  @Override
  public void disableMouseCursor() {
    delegate.disableMouseCursor();
    mouseCursor = null;
  }

  @Override
  public int createTextureAtlas(final int atlasWidth, final int atlasHeight) {
    return delegate.createTextureAtlas(atlasWidth, atlasHeight);
  }

  @Override
  public void clearTextureAtlas(final int atlasTextureId) {
    delegate.clearTextureAtlas(atlasTextureId);
  }

  @Nonnull
  @Override
  public Image loadImage(@Nonnull final String filename) {
    return delegate.loadImage(filename);
  }

  @Nullable
  @Override
  public Image loadImage(@Nonnull final ByteBuffer imageData, final int imageWidth, final int imageHeight) {
    return delegate.loadImage(imageData, imageWidth, imageHeight);
  }

  @Override
  public void addImageToAtlas(@Nonnull Image image, int atlasX, int atlasY, int atlasTextureId) {
    delegate.addImageToAtlas(image, atlasX, atlasY, atlasTextureId);
  }

  @Override
  public int createNonAtlasTexture(@Nonnull final Image image) {
    return delegate.createNonAtlasTexture(image);
  }

  @Override
  public void deleteNonAtlasTexture(final int textureId) {
    delegate.deleteNonAtlasTexture(textureId);
  }

  @Override
  public boolean existsNonAtlasTexture(final int textureId) {
    return delegate.existsNonAtlasTexture(textureId);
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
    delegate.addQuad(x, y, width, height, color1, color2, color3, color4, textureX, textureY, textureWidth, textureHeight, textureId);
  }

  @Override
  public void beginBatch(@Nonnull final BlendMode blendMode, final int textureId) {
    delegate.beginBatch(blendMode, textureId);
  }

  @Override
  public int render() {
    return delegate.render();
  }

  @Override
  public void removeImageFromAtlas(
          @Nonnull final Image image,
          final int atlasX,
          final int atlasY,
          final int imageWidth,
          final int imageHeight,
          final int atlasTextureId) {
    delegate.removeImageFromAtlas(image, atlasX, atlasY, imageWidth, imageHeight, atlasTextureId);
  }

  @Override
  public void useHighQualityTextures(final boolean shouldUseHighQualityTextures) {
    delegate.useHighQualityTextures(shouldUseHighQualityTextures);
  }

  @Override
  public void fillRemovedImagesInAtlas(final boolean shouldFill) {
    delegate.fillRemovedImagesInAtlas(shouldFill);
  }
}
