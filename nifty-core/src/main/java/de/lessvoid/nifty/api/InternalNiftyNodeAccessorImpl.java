package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;

final class InternalNiftyNodeAccessorImpl extends NiftyNodeAccessor {
    @Override
    public InternalNiftyNode getInternalNiftyNode(final NiftyNode niftyNode) {
      return niftyNode.getImpl();
    }

    @Override
    public NiftyCanvas createNiftyCanvas(final InternalNiftyCanvas impl) {
      return new NiftyCanvas(impl);
    }
}
