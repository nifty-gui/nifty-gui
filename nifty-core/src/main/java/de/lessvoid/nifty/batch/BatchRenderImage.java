package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;
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
  private final TextureAtlasGenerator generator;
  @Nonnull
  private final String filename;
  @Nonnull
  private final BatchRenderBackend renderBackend;
  @Nonnull
  private final Image image;

  private int x;
  private int y;
  private boolean uploaded;

  public BatchRenderImage(
      @Nonnull final Image image,
      @Nonnull final TextureAtlasGenerator generator,
      @Nonnull final String filename,
      @Nonnull final BatchRenderBackend renderBackend) {
    this.image = image;
    this.generator = generator;
    this.filename = filename;
    this.renderBackend = renderBackend;

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

  public void upload() {
    if (uploaded) {
      return;
    }
    try {
      Result result = generator.addImage(image.getWidth(), image.getHeight(), filename, 5);
      renderBackend.addImageToTexture(image, result.getX(), result.getY());
      x = result.getX();
      y = result.getY();
      uploaded = true;
      log.finer("image [" + filename + "] uploaded (texture atlas)");
    } catch (TextureAtlasGeneratorException e) {
      log.severe("Image [" + filename + "] did not fit into the texture atlas and will be missing in your screen");
   }
  }

  public void unload() {
    if (!uploaded) {
      return;
    }
    Result result = generator.removeImage(filename);
    if (result == null) {
      log.severe("For some reason the image, while its uploaded, is not part of the texture generator.");
      return;
    }
    renderBackend.removeFromTexture(image, result.getX(), result.getY(), result.getOriginalImageWidth(), result.getOriginalImageHeight());
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
