package de.lessvoid.nifty.api;

import org.jglfont.JGLFont;

import de.lessvoid.nifty.internal.accessor.NiftyFontAccessor;

public class NiftyFont {
  private final JGLFont font;

  NiftyFont(final JGLFont font) {
    this.font = font;
  }

  JGLFont getJGLFont() {
    return font;
  }

  /**
   * Return the width of the given text String in px using.
   *
   * @param text the String to get the width for
   * @return the width in px of the String
   */
  public int getWidth(final String text) {
    return font.getStringWidth(text);
  }

  /**
   * Return the height of the font.
   *
   * @return the height in px
   */
  public int getHeight() {
    return font.getHeight();
  }

  /**
   * Return the width in px of the given character including kerning information taking the next character into account.
   *
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or {@code -1} when no information for the character is available
   */
  public int getCharacterWidth(final char currentCharacter, final char nextCharacter, final float size) {
    return font.getCharacterWidth(currentCharacter, nextCharacter, size);
  }

  static {
    NiftyFontAccessor.DEFAULT = new InternalNiftyFontAccessorImpl();
  }
}
