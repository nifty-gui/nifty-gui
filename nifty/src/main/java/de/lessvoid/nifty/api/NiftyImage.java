package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.InternalNiftyImage;
import de.lessvoid.nifty.internal.accessor.NiftyImageAccessor;

/**
 * A image that can be rendered into a NiftyCanvas or that can be used as a background image for a NiftyNode.
 * @author void
 */
public final class NiftyImage {
  private final InternalNiftyImage impl;

  /**
   * Please use one of the {@link Nifty#createImage()} methods to create a new NiftyImage. You're not supposed to
   * create an instance of this class directly and you're not supposed to extend from this class.
   */
  private NiftyImage(final InternalNiftyImage impl) {
    this.impl = impl;
  }

  /**
   * Get the width of this texture.
   * @return the texture width
   */
  public int getWidth() {
    return impl.getWidth();
  }

  /**
   * Get the height of this texture.
   * @return the texture height
   */
  public int getHeight() {
    return impl.getHeight();
  }

  // package private methods

  InternalNiftyImage getImpl() {
    return impl;
  }

  static NiftyImage newInstance(final InternalNiftyImage internalImage) {
    return new NiftyImage(internalImage);
  }

  static {
    NiftyImageAccessor.DEFAULT = new InternalNiftyImageAccessorImpl();
  }
}
