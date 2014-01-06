package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.batch.spi.BatchRendererTexture;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * This only really carries the x and y position of the image in the texture atlas as well as the width and height of
 * the image.
 * @author void
 */
public class BatchRenderImage implements RenderImage {
  @Nonnull
  private static final Logger log = Logger.getLogger(BatchRenderImage.class.getName());

  @Nonnull
  private final BatchRendererTexture texture;

  @Nonnull
  private final String filename;
  @Nonnull
  private final BatchRendererTexture.Image image;

  private int x;
  private int y;
  private boolean uploaded;

  public BatchRenderImage(
      @Nonnull final BatchRendererTexture.Image image,
      @Nonnull final String filename,
      @Nonnull final BatchRendererTexture texture) {
    this.image = image;
    this.filename = filename;
    this.texture = texture;

    // the real x and y will be calculated in the upload() method
    this.x = 0;
    this.y = 0;
    this.uploaded = false;
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

  public void tryUpload() throws TextureAtlasGeneratorException {
    if (uploaded) {
      return;
    }
    Result result = texture.getGenerator().addImage(image.getWidth(), image.getHeight(), filename, 5);
    texture.addImageToTexture(image, result.getX(), result.getY());
    x = result.getX();
    y = result.getY();
    uploaded = true;
    log.finer("image [" + filename + "] uploaded (texture atlas)");
  }

  public void upload() {
    try {
      tryUpload();
    } catch (TextureAtlasGeneratorException e) {
      log.severe("Image [" + filename + "] did not fit into the texture atlas and will be missing in your screen");
    }
  }

  public void unload() {
    if (!uploaded) {
      return;
    }
    Result result = texture.getGenerator().removeImage(filename);
    if (result == null) {
      log.severe("For some reason the image, while its uploaded, is not part of the texture generator.");
      return;
    }
    texture.removeFromTexture(image, result.getX(), result.getY(), result.getOriginalImageWidth(), result.getOriginalImageHeight());
    uploaded = false;
    log.finer("image [" + filename + "] unloaded (texture atlas)");
  }

  public boolean isUploaded() {
    return uploaded;
  }

  public void markAsUnloaded() {
    uploaded = false;
    log.finer("image [" + filename + "] marked as unloaded (texture atlas)");
  }

  @Override
  @Nonnull
  public String toString() {
    return super.toString() + " {" + filename + "}";
  }
}
