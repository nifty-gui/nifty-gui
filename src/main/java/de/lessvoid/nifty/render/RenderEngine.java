package de.lessvoid.nifty.render;

import java.util.Set;

import de.lessvoid.nifty.tools.Color;

/**
 * RenderEngine interface.
 * @author void
 */
public interface RenderEngine {

  /**
   * Get Width of Display mode.
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get Height of Display mode.
   * @return height of display mode
   */
  int getHeight();

  /**
   * Clear the screen.
   */
  void clear();

  /**
   * Create a new Image.
   * @param name file name to use
   * @param filterLinear filter
   * @return RenderImage instance
   */
  RenderImage createImage(String name, boolean filterLinear);

  /**
   * Create a new RenderFont.
   * @param name name of the font
   * @return RenderFont instance
   */
  RenderFont createFont(String name);

  /**
   * render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  void renderQuad(int x, int y, int width, int height);

  /**
   * Render Image.
   * @param image the image to render
   * @param x the x position on the screen
   * @param y the y position on the screen
   * @param width the width
   * @param height the height
   */
  void renderImage(RenderImage image, int x, int y, int width, int height);

  /**
   * renderText.
   * @param font font
   * @param text text
   * @param x x
   * @param y y
   * @param selectionStart selection start
   * @param selectionEnd selection end
   * @param textSelectionColor color for text selections
   */
  void renderText(
      RenderFont font, String text, int x, int y, int selectionStart, int selectionEnd, Color textSelectionColor);

  /**
   * Set a new color.
   * @param colorParam new current color to set
   */
  void setColor(Color colorParam);

  /**
   * return true when color has been changed.
   * @return color changed
   */
  boolean isColorChanged();

  /**
   * Move to the given x/y position.
   * @param xParam x
   * @param yParam y
   */
  void moveTo(float xParam, float yParam);

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  void enableClip(int x0, int y0, int x1, int y1);

  /**
   * Disable the clipping.
   */
  void disableClip();

  /**
   * Set RenderTextSize.
   * @param size size
   */
  void setRenderTextSize(float size);

  /**
   * set image size.
   * @param scale new image size
   */
  void setImageScale(float scale);

  /**
   * set global position.
   * @param xPos x
   * @param yPos y
   */
  void setGlobalPosition(float xPos, float yPos);

  /**
   * save given states.
   * @param statesToSave set of renderstates to save
   */
  void saveState(Set < RenderState > statesToSave);

  /**
   * restore states.
   */
  void restoreState();
}
