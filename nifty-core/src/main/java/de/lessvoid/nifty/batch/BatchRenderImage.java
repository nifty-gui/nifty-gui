package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.spi.render.RenderImage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author void
 */
public class BatchRenderImage implements RenderImage {
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchRenderImage.class.getName());
  @Nonnull
  private static Map<Integer, TextureSize> textureSizes = new HashMap<Integer, TextureSize>(); // provides the size of a texture represented by a specific texture id
  @Nonnull
  private final Image image; // the image in the format needed by the rendering backend
  @Nonnull
  private final String filename; // the filename associated with this image
  @Nonnull
  private final BatchRenderBackend renderBackend; // the rendering backend to delegate low level texture handling to
  @Nonnull
  private TextureAtlasGenerator generator; // mainly used to determine whether the image will fit in the specified atlas (the "brain" of the texture atlas)
  private int x; // the x location of the image in an atlas, or 0 for non-atlas images
  private int y; // the y location of the image in an atlas, or 0 for non-atlas images
  private int textureId; // the texture id of the atlas texture this image part of, or the texture id of the non-atlas texture representing this image
  private boolean isUploaded; // whether this image was uploaded (created as a texture) yet
  private boolean shouldUnload; // whether this image should be unloaded when unload() is called on it
  private boolean uploadFailed; // will be set to true if this image already failed at an attempted upload
  @Nullable
  private Result result; // the result returned by processing this image with a TextureAtlasGenerator

  /**
   * @param image The image in the format needed by the rendering backend
   * @param filename The filename associated with this image
   * @param renderBackend The rendering backend to delegate low level texture handling to
   * @param generator Mainly used to determine whether the image will fit in the specified atlas (the "brain" of the texture atlas)
   * @param atlasTextureId The texture id of the atlas to use to attempt to upload the image to.
   * @param shouldUnload Whether or not to unload the image between screens.
   */
  public BatchRenderImage(
      @Nonnull final Image image,
      @Nonnull final String filename,
      @Nonnull final BatchRenderBackend renderBackend,
      @Nonnull final TextureAtlasGenerator generator,
      final int atlasTextureId,
      final boolean shouldUnload) {
    this.image = image;
    this.filename = filename;
    this.generator = generator;
    this.renderBackend = renderBackend;
    this.shouldUnload = shouldUnload;
    // the final x, y, and textureId will be calculated in the upload() method
    textureId = atlasTextureId;
    x = 0;
    y = 0;
    isUploaded = false;
    uploadFailed = false;
  }

  public static class TextureSize {
    private final int width;
    private final int height;

    public TextureSize(final int width, final int height) {
      this.width = width;
      this.height = height;
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }
  }

  @Nullable
  public static TextureSize getTextureSize(final int textureId) {
    return textureSizes.get(textureId);
  }

  @Override
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int getHeight() {
    return image.getHeight();
  }

  @Override
  public void dispose() {
  }  

  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }

  public int getTextureId() {
    return textureId;
  }

  public boolean isUploaded() {
    return isUploaded;
  }

  public void markAsUnloaded() {
    if (shouldUnload) {
      isUploaded = false;
      uploadFailed = false;
      log.fine("image [" + filename + "] marked as unloaded");
    }
  }

  public String toString() {
    return super.toString() + " {" + filename + "}";
  }

  // You can only call this once. After that, the image will either be uploaded or will have failed to upload.
  // In either case, calling it again has no effect. Try the reUpload method if the upload fails.
  public void upload() {
    if (isUploaded || uploadFailed) {
      return;
    }

    preProcessImageUpload();

    if (imageWillFitInAtlas()) {
      uploadImageToAtlas();
    } else if (imageExceedsAtlasTolerance()) {
      uploadNonAtlasImage();
    } else {
      uploadFailedBecauseAtlasIsFull();
    }
  }

  // Reattempts uploading with the specified atlas texture id and texture atlas generator.
  // Don't bother calling this if the uploadFailedPermanently method returns true.
  public void reUpload(final int atlasTextureId, @Nonnull final TextureAtlasGenerator generator) {
    textureId = atlasTextureId;
    this.generator = generator;
    isUploaded = false;
    uploadFailed = false;
    upload();
  }

  public boolean uploadFailedPermanently() {
    // If we've already failed once before, and if the image exceeds atlas tolerance, we'll never succeed.
    // (The reason why is that currently all atlases have the same tolerance, so if you fail uploading to one atlas due
    // to tolerance, you fail uploading to all atlases due to tolerance, regardless of how empty they might be).
    return uploadFailed && imageExceedsAtlasTolerance();
  }

  public void unload() {
    if (!isUploaded || !shouldUnload) {
      return;
    }

    preProcessImageUnloading();

    if (imageExistsInAtlas()) {
      unloadImageFromAtlas();
    } else if (existsAsNonAtlasImage()) {
      unloadNonAtlasImage();
    } else {
      unloadFailed();
    }

    isUploaded = false;
  }

  // Internal implementations

  private void preProcessImageUpload() {
    result = generator.addImage(image.getWidth(), image.getHeight(), filename);
  }

  private boolean imageWillFitInAtlas() {
    return result != null;
  }

  private void uploadImageToAtlas() {
    assert result != null;
    renderBackend.addImageToAtlas(image, result.getX(), result.getY(), textureId);
    BatchRenderImage.registerTextureSize(textureId, generator.getAtlasWidth(), generator.getAtlasHeight());
    x = result.getX();
    y = result.getY();
    isUploaded = true;
    log.info("Image [" + filename + "] uploaded to atlas (atlas texture id: " + textureId + ").");
  }

  private static void registerTextureSize(final int textureId, final int textureWidth, final int textureHeight) {
    BatchRenderImage.textureSizes.put(textureId, new TextureSize(textureWidth, textureHeight));
  }

  private boolean imageExceedsAtlasTolerance() {
    return ! generator.shouldAddImage(image.getWidth(), image.getHeight());
  }

  private void uploadNonAtlasImage() {
    int textureId = createNonAtlasTexture();

    if (isCreatedNonAtlasTexture(textureId)) {
      uploadNonAtlasImageSuccessful(textureId);
    } else {
      uploadNonAtlasImageFailed();
    }
  }

  private int createNonAtlasTexture() {
    return renderBackend.createNonAtlasTexture(image);
  }

  private boolean isCreatedNonAtlasTexture(final int textureId) {
    return renderBackend.existsNonAtlasTexture(textureId);
  }

  private void uploadNonAtlasImageSuccessful(final int textureId) {
    this.textureId = textureId;
    BatchRenderImage.registerTextureSize(textureId, getWidth(), getHeight());
    isUploaded = true;
    log.info("Image [" + filename + "] is not within atlas tolerance and has been created as a non-atlas texture " +
            "(texture id: " + textureId + ").");
  }

  private void uploadNonAtlasImageFailed() {
    log.severe("Image [" + filename + "] is not within atlas tolerance but could not be created as a non-atlas " +
            "texture.\nThis image will be missing from your screen. Some of the possible causes could be:\n" +
            "1) Your BatchRenderBackend doesn't support non-atlas textures.\n" +
            "2) There is a compatibility issue between your gpu and the image (size, format, file type, etc).\n" +
            "3) You don't have enough memory to create the image.\n\n" +
            "For support, please create a new issue at https://github.com/void256/nifty-gui");
    uploadFailed = true;
    textureId = -1;
  }

  private void uploadFailedBecauseAtlasIsFull() {
    log.info("Image [" + filename + "] did not fit into the texture atlas, yet it is within atlas tolerance.\n" +
                "The current atlas (atlas texture id: " + textureId + ") is too full to hold this image.");
    uploadFailed = true;
    textureId = -1;
  }

  private void preProcessImageUnloading() {
    result = generator.removeImage(filename);
  }

  private boolean imageExistsInAtlas() {
    return result != null;
  }

  private void unloadImageFromAtlas() {
    assert result != null;
    renderBackend.removeImageFromAtlas(
            image,
            result.getX(),
            result.getY(),
            result.getOriginalImageWidth(),
            result.getOriginalImageHeight(),
            textureId);
    log.info("Image [" + filename + "] unloaded from texture atlas (atlas texture id: " + textureId + ").");
  }

  private static void deregisterTextureSize(final int textureId) {
    BatchRenderImage.textureSizes.remove(textureId);
  }

  private boolean existsAsNonAtlasImage() {
    return renderBackend.existsNonAtlasTexture(textureId);
  }

  private void unloadNonAtlasImage() {
    renderBackend.deleteNonAtlasTexture(textureId);
    deregisterTextureSize(textureId);
    log.info("Image [" + filename + "] unloaded (non-atlas texture, texture id: " + textureId + ")");
  }

  private void unloadFailed() {
    log.warning("Failed to unload image [" + filename + "] because its associated texture (texture id: " + textureId +
            ") could not be found.");
  }
}
