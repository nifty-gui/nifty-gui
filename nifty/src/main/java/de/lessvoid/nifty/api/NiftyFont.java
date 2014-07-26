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

  static {
    NiftyFontAccessor.DEFAULT = new InternalNiftyFontAccessorImpl();
  }
}
