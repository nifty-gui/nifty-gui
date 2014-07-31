package de.lessvoid.nifty.api;


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
  Size calculateMinSize(NiftyNode niftyNode);

  /**
   * Just a simple Size class that will transport a width and height value.
   */
  public class Size {
    public float width;
    public float height;
  }
}
