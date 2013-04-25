package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;

public abstract class NiftyCanvasAccessor {
    public static NiftyCanvasAccessor DEFAULT;

    public static NiftyCanvasAccessor getDefault() {
        if (DEFAULT != null) {
            return DEFAULT;
        }

        // invokes static initializer of Nifty.class that will assign value to the DEFAULT field above
        Class<?> c = NiftyCanvas.class;
        try {
            Class.forName(c.getName(), true, c.getClassLoader());
        } catch (ClassNotFoundException ex) {
            assert false : ex;
        }
        assert DEFAULT != null : "The DEFAULT field must be initialized";
        return DEFAULT;
    }

    public abstract InternalNiftyCanvas getInternalNiftyCanvas(NiftyCanvas niftyCanvas);
}
