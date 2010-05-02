package de.lessvoid.nifty.spi.render;


/**
 * RenderImage interface.
 * @author void
 */
public interface RenderImage {

  /**
   * Get the width of the image.
   * @return width of image in pixel
   */
  int getWidth();

  /**
   * Get the height of the image.
   * @return height of image in pixel
   */
  int getHeight();

  /**
   * This RenderImage is not needed anymore. You should dispose
   * any resources you allocated for this image.
   */
  void dispose();
}

