package de.lessvoid.nifty.batch;

import java.util.logging.Logger;

import de.lessvoid.nifty.batch.TextureAtlasGenerator.Result;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * This only really carries the x and y position of the image in the texture atlas as well as the width and height of
 * the image.
 * @author void
 */
public class BatchRenderImage implements RenderImage {
  private static final Logger log = Logger.getLogger(BatchRenderImage.class.getName());

  private final TextureAtlasGenerator generator;
  private final String filename;
  private final BatchRenderBackend renderBackend;
  private final Image image;
  private int x;
  private int y;
  private boolean uploaded;

  public BatchRenderImage(
      final Image image,
      final TextureAtlasGenerator generator,
      final String filename,
      final BatchRenderBackend renderBackend) {
    this.image = image;
    this.generator = generator;
    this.filename = filename;
    this.renderBackend = renderBackend;

    // the real x and y will be calculated in the upload() method
    this.x = 0;
    this.y = 0;
    this.uploaded = false;
  }

  public int getWidth() {
    return image.getWidth();
  }

  public int getHeight() {
    return image.getHeight();
  }

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

  public String toString() {
    return super.toString() + " {" + filename + "}";
  }
}
