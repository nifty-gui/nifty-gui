package de.lessvoid.nifty.internal.accessor;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public abstract class NiftyAccessor {
    public static NiftyAccessor DEFAULT;

    public static NiftyAccessor getDefault() {
        if (DEFAULT != null) {
            return DEFAULT;
        }

        // invokes static initializer of Nifty.class that will assign value to the DEFAULT field above
        Class<?> c = Nifty.class;
        try {
            Class.forName(c.getName(), true, c.getClassLoader());
        } catch (ClassNotFoundException ex) {
            assert false : ex;
        }
        assert DEFAULT != null : "The DEFAULT field must be initialized";
        return DEFAULT;
    }

    public abstract NiftyRenderDevice getRenderDevice(Nifty nifty);
}
