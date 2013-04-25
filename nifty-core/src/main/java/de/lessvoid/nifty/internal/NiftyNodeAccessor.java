package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;

public abstract class NiftyNodeAccessor {
    public static NiftyNodeAccessor DEFAULT;

    public static NiftyNodeAccessor getDefault() {
        if (DEFAULT != null) {
            return DEFAULT;
        }

        // invokes static initializer of Nifty.class that will assign value to the DEFAULT field above
        Class<?> c = NiftyNode.class;
        try {
            Class.forName(c.getName(), true, c.getClassLoader());
        } catch (ClassNotFoundException ex) {
            assert false : ex;
        }
        assert DEFAULT != null : "The DEFAULT field must be initialized";
        return DEFAULT;
    }

    public abstract InternalNiftyNode getInternalNiftyNode(NiftyNode niftyNode);
    public abstract NiftyCanvas createNiftyCanvas(InternalNiftyCanvas impl);
}
