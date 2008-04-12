package de.lessvoid.nifty.render;

/**
 * RenderImage interface.
 * @author void
 *
 */
public interface RenderImage {

  /**
   * sub image type.
   * @author void
   */
  public enum SubImageMode {

    /**
     * disabled - use normal rendering.
     */
    Disabled,

    /**
     * just scale it.
     */
    Scale,

    /**
     * use the resize hint.
     */
    ResizeHint;
  }

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
   * Render the image.
   * @param x x
   * @param y y
   * @param width w
   * @param height h
   */
  void render(final int x, final int y, final int width, final int height);

  /**
   * Set the sub image mode for this image.
   * @param newSubImageMode the SubImageMode
   */
  void setSubImageMode(SubImageMode newSubImageMode);

  /**
   * Set the sub image dimensions.
   * @param newSubImageX x
   * @param newSubImageY y
   * @param newSubImageW w
   * @param newSubImageH h
   */
  void setSubImage(int newSubImageX, int newSubImageY, int newSubImageW, int newSubImageH);

  /**
   * Resize hint.
   * @param resizeHint new String with resize hint information.
   */
  void setResizeHint(String resizeHint);
}

