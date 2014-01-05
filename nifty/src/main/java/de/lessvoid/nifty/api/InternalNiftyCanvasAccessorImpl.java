package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.accessor.NiftyCanvasAccessor;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;

final class InternalNiftyCanvasAccessorImpl extends NiftyCanvasAccessor {
    @Override
    public InternalNiftyCanvas getInternalNiftyCanvas(final NiftyCanvas niftyCanvas) {
      return niftyCanvas.getImpl();
    }
}
