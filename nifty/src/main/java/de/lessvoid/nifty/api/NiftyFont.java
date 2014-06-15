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

  static {
    NiftyFontAccessor.DEFAULT = new InternalNiftyFontAccessorImpl();
  }
}
