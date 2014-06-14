package de.lessvoid.nifty.internal.accessor;

import de.lessvoid.nifty.api.NiftyImage;
import de.lessvoid.nifty.internal.InternalNiftyImage;

public abstract class NiftyImageAccessor {
    public static NiftyImageAccessor DEFAULT;

    public static NiftyImageAccessor getDefault() {
        if (DEFAULT != null) {
            return DEFAULT;
        }

        // invokes static initializer of Nifty.class that will assign value to the DEFAULT field above
        Class<?> c = NiftyImage.class;
        try {
            Class.forName(c.getName(), true, c.getClassLoader());
        } catch (ClassNotFoundException ex) {
            assert false : ex;
        }
        assert DEFAULT != null : "The DEFAULT field must be initialized";
        return DEFAULT;
    }

    public abstract InternalNiftyImage getInternalNiftyImage(NiftyImage niftyImage);
}
