package de.lessvoid.nifty.api;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * The main control class of all things Nifty.
 * @author void
 */
public class Nifty {

  
  public Nifty(final NiftyRenderDevice niftyRenderDevice) {
  }

  /**
   * Update.
   */
  public void update() {
    
  }

  /**
   * Render.
   */
  public void render() {
    
  }

  /**
   * Create a new Node.
   * @return a fresh NiftyNode
   */
  public NiftyNode createNode() {
    return new de.lessvoid.nifty.internal.NiftyNode();
  }

  /**
   * Create a new NiftyNode that will be forced to have the given width and height.
   * @param width width of the new NiftyNode
   * @param height height of the new NiftyNode
   * @return a new NiftyNode
   */
  public NiftyNode createNode(final int width, final int height) {
    NiftyNode result = new de.lessvoid.nifty.internal.NiftyNode();
    result.setWidthConstraint(UnitValue.px(width));
    result.setHeightConstraint(UnitValue.px(height));
    return result;
  }

  /**
   * Set the rootNode for this Nifty instance. There can only be a single root node. The root node will be the node
   * that Nifty will start rendering.
   * @param rootNode the NiftyNode that will be the root node for this Nifty
   */
  public void setRootNode(final NiftyNode rootNode) {
  }

  /**
   * Get the width of the current screen mode.
   * @return width of the current screen
   */
  public int getScreenWidth() {
    return 0;
  }

  /**
   * Get the height of the current screen mode.
   * @return height of the current screen
   */
  public int getScreenHeight() {
    return 0;
  }

}
