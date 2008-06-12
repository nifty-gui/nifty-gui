package de.lessvoid.nifty.render;

import de.lessvoid.nifty.tools.Color;

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
  int getVisibleCharactersFromStart(String text, int width);

  /**
   * Get character index into the given text that is no more pixel as the given width.
   * @param text the string to check
   * @param width the minimum width
   * @return the character index into the string.
   */
  int getVisibleCharactersFromEnd(String text, int width);

  /**
   * get index into text from a given pixel position.
   * @param text text string
   * @param pixel pixel index
   * @param size font size
   * @return index into text string
   */
  int getCharacterIndexFromPixelPosition(String text, int pixel, float size);

  /**
   * Render the given text at the given position.
   * @param text text to render
   * @param x x position
   * @param y y position
   * @param fontColor font color
   * @param size size
   */
  void render(String text, int x, int y, Color fontColor, float size);
}
