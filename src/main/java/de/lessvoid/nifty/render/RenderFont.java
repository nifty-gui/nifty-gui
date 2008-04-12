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
}
