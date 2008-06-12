package de.lessvoid.nifty.render;

import de.lessvoid.nifty.tools.Color;

/**
 * Nifty RenderDevice.
 * @author void
 */
public interface RenderDevice {

  /**
   * Create a new RenderImage.
   * @param filename filename
   * @param filterLinear filter
   * @return RenderImage
   */
  RenderImage createImage(String filename, boolean filterLinear);

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  RenderFont createFont(String filename);

  /**
   * Get Width.
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get Height.
   * @return height of display mode
   */
  int getHeight();

  /**
   * Clear the Screen.
   */
  void clear();

  /**
   * Render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   */
  void renderQuad(int x, int y, int width, int height, Color color);

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  void enableClip(int x0, int y0, int x1, int y1);

  /**
   * Disable Clipping.
   */
  void disableClip();
}
