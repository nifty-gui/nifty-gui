package de.lessvoid.nifty.render.spi;

import de.lessvoid.nifty.tools.Color;

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
   * Render the image.
   * @param x x
   * @param y y
   * @param width w
   * @param height h
   * @param color color
   * @param imageScale image scale
   */
  void render(int x, int y, int width, int height, Color color, float imageScale);

  /**
   * Render a sub image of this image.
   * @param x x
   * @param y y
   * @param w w
   * @param h h
   * @param srcX source x
   * @param srcY source y
   * @param srcW source width
   * @param srcH source height
   * @param color color
   */
  void render(int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH, Color color, float scale, int centerX, int centerY);
}

