package de.lessvoid.nifty.api;

import org.jglfont.JGLFont;

import de.lessvoid.nifty.internal.accessor.NiftyFontAccessor;

final class InternalNiftyFontAccessorImpl extends NiftyFontAccessor {
    @Override
    public JGLFont getJGLFont(final NiftyFont niftyFont) {
      return niftyFont.getJGLFont();
    }
}
