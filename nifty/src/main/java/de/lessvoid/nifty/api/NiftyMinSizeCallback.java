package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.math.Vec2;

/**
 * The NiftyMinSizeCallback allows you to provide a minimal size for a NiftyNode. This will be used when the minSize-
 * Feature is enabled. You should provide the minimal size of the NiftyNode content and Nifty will automatically
 * set width and height constraints for the NiftyNode.
 *
 * This callback will only be used when the NiftyNode does not have any existing constraints.
 *
 * @author void
 */
public interface NiftyMinSizeCallback {

  /**
   * Calculate the minimal size for the given NiftyNode.
   * @param niftyNode the NiftyNode to calculate the size for
   * @return a Vec2 representing width/height of the niftyNode
   */
  Vec2 calculateMinSize(NiftyNode niftyNode);
}
