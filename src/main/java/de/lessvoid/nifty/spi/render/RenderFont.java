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
   * The height of the font in pixel.
   * @return font height in pixel.
   */
  int getHeight();

  /**
   * Return the advance of the given character including kerning information.
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or null when no information for the character is available
   */
  Integer getCharacterAdvance(char currentCharacter, char nextCharacter, float size);

  /**
   * This RenderFont is not needed anymore. You should dispose
   * any resources you allocated for this font.
   */
  void purge();
}
