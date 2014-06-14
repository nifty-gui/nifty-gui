package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.InternalNiftyImage;
import de.lessvoid.nifty.internal.accessor.NiftyImageAccessor;

final class InternalNiftyImageAccessorImpl extends NiftyImageAccessor {
    @Override
    public InternalNiftyImage getInternalNiftyImage(final NiftyImage niftyImage) {
      return niftyImage.getImpl();
    }
}
