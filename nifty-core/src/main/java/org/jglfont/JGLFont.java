package org.jglfont;


/**
 * A JGLFont ready to be rendered on screen.
 * @author void
 */
public interface JGLFont {

  /**
   * Output text at the given x and y positon. White will be used for the complete text.
   *
   * @param x x position
   * @param y y position
   * @param text text to output
   */
  void renderText(int x, int y, String text);

  /**
   * Output text at the given x and y position using the given color and the r,g,b,a components.
   *
   * @param x x position
   * @param y y position
   * @param text the text to output
   * @param sizeX x scale factor of the text (this should be apply with x,y as the origin)
   * @param sizeY y scale factor of the text (this should be apply with x,y as the origin)
   * @param r red
   * @param g green
   * @param b blue
   * @param a alpha
   */
  void renderText(int x, int y, String text, float sizeX, float sizeY, float r, float g, float b, float a);

  /**
   * Get the width of a single character.
   *
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @return the width of the character when followed by the given next character
   */
  int getCharacterWidth(int currentCharacter, int nextCharacter);

  /**
   * The same as the getCharacterWidth(int, char) method but takes a scaled size into account.
   */
  int getCharacterWidth(int currentCharacter, int nextCharacter, float size);

  /**
   * Returns the width of the given String.
   * @param text the text
   * @return the width
   */
  int getStringWidth(String text);

  /**
   * The same as the getStringWidthInternal(String) method that takes the given size into account.
   */
  int getStringWidth(String text, float size);

  /**
   * The height of the given font (the lineHeight property of the font data)
   * @return
   */
  int getHeight();

  /**
   * Allows to transfer custom data to the FontRenderer. The instance you provide here will be set in the
   * beforeRender() method of the FontRenderer.
   * @param customRenderState this instance will be forwarded to the fontRenderer.beforeRender() method
   */
  void setCustomRenderState(Object customRenderState);
}
