package de.lessvoid.nifty.render;

/**
 * RenderFont Interface.
 * @author void
 */
public interface RenderFont {

  /**
   * Get width in pixel of given text.
   * @param text the text to measure.
   * @return the pixel width of the given text
   */
  int getWidth(String text);

  /**
   * The height of the font in pixel.
   * @return font height in pixel.
   */
  int getHeight();

  /**
   * Get character index into the given text that is no more pixel as the given width.
   * @param text the string to check
   * @param width the minimum width
   * @return the character index into the string.
   */
  int getFittingOffset(String text, int width);

  /**
   * Get character index into the given text that is no more pixel as the given width.
   * @param text the string to check
   * @param width the minimum width
   * @return the character index into the string.
   */
  int getFittingOffsetBackward(String text, int width);

  /**
   * get index into text from a given pixel position.
   * @param text text string
   * @param pixel pixel index
   * @param size font size
   * @return index into text string
   */
  int getIndexFromPixel(final String text, final int pixel, final float size);

  /**
   * set selection.
   * @param selectionStart selection start
   * @param selectionEnd selection end
   */
  void setSelection(int selectionStart, int selectionEnd);

  /**
   * Render the given text at the given position.
   * @param text text to render
   * @param x x position
   * @param y y position
   */
  void render(String text, int x, int y);

  /**
   * Set font size to the given value.
   * @param size new font size
   */
  void setSize(float size);

  /**
   * Set font color to the given values.
   * @param r red
   * @param g green
   * @param b blue
   * @param a alpha
   */
  void setColor(float r, float g, float b, float a);

  /**
   * render the font with the default color.
   */
  void setDefaultColor();
}
