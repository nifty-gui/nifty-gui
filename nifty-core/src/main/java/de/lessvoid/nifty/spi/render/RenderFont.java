package de.lessvoid.nifty.spi.render;


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
   * Get width in pixel of given text.
   * @param text the text to measure.
   * @param size size
   * @return the pixel width of the given text
   */
  int getWidth(String text, float size);

  /**
   * The height of the font in pixel.
   * @return font height in pixel.
   */
  int getHeight();

  /**
   * Return the advance of the given character including kerning information.
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or -1 when no information for the character is available
   */
  int getCharacterAdvance(char currentCharacter, char nextCharacter, float size);

  /**
   * This RenderFont is not needed anymore. You should dispose
   * any resources you allocated for this font.
   */
  void dispose();
}
