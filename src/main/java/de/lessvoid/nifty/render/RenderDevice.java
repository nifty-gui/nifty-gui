package de.lessvoid.nifty.render;

/**
 * Nifty RenderDevice.
 * @author void
 */
public interface RenderDevice {

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
   * Create a new RenderImage.
   * @param filename filename
   * @param filter filter
   * @return RenderImage
   */
  RenderImage createImage(String filename, boolean filter);

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  RenderFont createFont(String filename);

  /**
   * Render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  void renderQuad(final int x, final int y, final int width, final int height);

  /**
   * Enable Blendmode.
   */
  void enableBlend();

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
