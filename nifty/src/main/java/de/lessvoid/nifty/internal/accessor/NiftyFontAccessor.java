package de.lessvoid.nifty.internal.accessor;

import org.jglfont.JGLFont;

import de.lessvoid.nifty.api.NiftyFont;

public abstract class NiftyFontAccessor {
    public static NiftyFontAccessor DEFAULT;

    public static NiftyFontAccessor getDefault() {
        if (DEFAULT != null) {
            return DEFAULT;
        }

        // invokes static initializer of Nifty.class that will assign value to the DEFAULT field above
        Class<?> c = NiftyFont.class;
        try {
            Class.forName(c.getName(), true, c.getClassLoader());
        } catch (ClassNotFoundException ex) {
            assert false : ex;
        }
        assert DEFAULT != null : "The DEFAULT field must be initialized";
        return DEFAULT;
    }

    public abstract JGLFont getJGLFont(NiftyFont niftyFont);
}
