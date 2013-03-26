package de.lessvoid.nifty.render;

import de.lessvoid.nifty.batch.BatchRenderImage;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * NiftyImage.
 * @author void
 */
public class NiftyImage {
  private RenderImage image;
  private ImageMode imageMode;
  private NiftyRenderEngine niftyRenderEngine;

  /**
   * create new NiftyImage.
   * @param createImage RenderImage
   */
  public NiftyImage(final NiftyRenderEngine niftyRenderEngine, final RenderImage createImage) {
    this.niftyRenderEngine = niftyRenderEngine;
    this.image = createImage;
    this.imageMode = ImageModeFactory.getSharedInstance().createImageMode(null, null);
  }

  /**
   * Get the width of the image.
   * @return width of image in pixel
   */
  public int getWidth() {
    return image.getWidth();
  }

  /**
   * Get the height of the image.
   * @return height of image in pixel
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Render the image using the given Box to specify the render attributes.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   * @param scale scale
   */
  public void render(final int x, final int y, final int width, final int height, final Color color, final float scale) {
    imageMode.render(niftyRenderEngine.getRenderDevice(), image, x, y, width, height, color, scale);
  }

  /**
   * Set a new sub image active state.
   * @param imageMode new type
   */
  public void setImageMode(final ImageMode imageMode) {
    this.imageMode = imageMode;
  }

  /**
   * Get the current ImageMode of this image. This can be used to modify the imageMode parameters dynamically.
   * @return the current ImageMode of this NiftyImage
   */
  public ImageMode getImageMode() {
    return this.imageMode;
  }

  /**
   * Reload the image data.
   */
  public void reload() {
    image = niftyRenderEngine.reload(image);

  }

  /**
   * This is a special method for the batched renderer. If you use the batched renderer and you have changed the
   * content of the backing texture through some external mechanism (external to Nifty that is, f.i. through jme3)
   * Nifty does not know about your change. In case of the batched renderer this is unfortunate since your modified
   * texture will never be uploaded to the texture atlas in this case.
   *
   * This method allows you to trigger that upload manual by marking this NiftyImage as unloaded Nifty will
   * automatically upload it the next time this NiftyImage is accessed. So you would change the backing texture and
   * then call this method to notify Nifty of your change.
   */
  public void markBatchRenderImageAsUnloaded() {
    if (!(image instanceof BatchRenderImage)) {
      return;
    }
    BatchRenderImage batchRenderImage = (BatchRenderImage) image;
    batchRenderImage.markAsUnloaded();
  }

  /**
   * Dispose the resources kept by this image.
   */
  public void dispose() {
    niftyRenderEngine.disposeImage(image);
  }
}
