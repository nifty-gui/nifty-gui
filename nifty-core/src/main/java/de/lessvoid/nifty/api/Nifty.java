package de.lessvoid.nifty.api;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyNode.ChildLayout;
import de.lessvoid.nifty.internal.InternalNiftyIdGenerator;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * The main control class of all things Nifty.
 * @author void
 */
public class Nifty {

  // The NiftyRenderDevice we'll forward all render calls to.
  private final NiftyRenderDevice renderDevice;

  // The list of root nodes.
  private final List<NiftyNode> rootNodes = new ArrayList<NiftyNode>();

  // The ChildLayout for the parent (!) of the root node! This actually places the root node on the screen since the
  // parent of the root node is always using the exact same size as the screen.
  private ChildLayout rootNodePlacementLayout = ChildLayout.Center;

  /**
   * Create a new Nifty instance.
   * @param renderDevice the NiftyRenderDevice this instance will be using
   */
  public Nifty(final NiftyRenderDevice renderDevice) {
    this.renderDevice = renderDevice;
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
   * You can change the way the root node is placed on the screen with this call. You just need to call this method
   * before you call any of the createRootNode() methods. By default this is set to ChildLayout.Center.
   *
   * @param rootNodePlacementLayout the new layout to place the root node on the screen
   */
  public void setRootNodePlacementLayout(final ChildLayout rootNodePlacementLayout) {
    this.rootNodePlacementLayout = rootNodePlacementLayout;
  }

  /**
   * Create a new root node. A root node is just a regular NiftyNode that forms the base node of a scene graph.
   * You can add several root nodes!
   * 
   * @return a new NiftyNode acting as the root of a Nifty scene graph
   */
  public NiftyNode createRootNode() {
    NiftyNode rootNodeInternal = createRootNodeInternal();
    rootNodes.add(rootNodeInternal);
    return rootNodeInternal;
  }

  /**
   * Create a new root node with a given width, height and child layout. A root node is just a regular NiftyNode that
   * forms the base node of a scene graph. You can add several root nodes!
   *
   * @param width the width of the root node
   * @param height the height of the root node
   * @param childLayout the childLayout for the root node (this determines the way any child nodes will be layed out
   * in the new rootNode)
   * 
   * @return a new NiftyNode acting as the root of a Nifty scene graph
   */
  public NiftyNode createRootNode(final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    NiftyNode rootNodeInternal = createRootNode();
    rootNodeInternal.setWidthConstraint(width);
    rootNodeInternal.setHeightConstraint(height);
    rootNodeInternal.setChildLayout(childLayout);
    return rootNodeInternal;
  }

  /**
   * @see #createRootNode(UnitValue, UnitValue, ChildLayout)
   *
   * Additionally this method will make the created root node the same size as the current screen.
   * 
   * @param childLayout the childLayout for the root node (this determines the way any child nodes will be layed out
   * in the new rootNode)
   * @return a new NiftyNode
   */
  public NiftyNode createRootNodeFullscreen(final ChildLayout childLayout) {
    return createRootNode(UnitValue.px(getScreenWidth()), UnitValue.px(getScreenHeight()), childLayout);
  }

  /**
   * Get the width of the current screen mode.
   * @return width of the current screen
   */
  public int getScreenWidth() {
    return renderDevice.getWidth();
  }

  /**
   * Get the height of the current screen mode.
   * @return height of the current screen
   */
  public int getScreenHeight() {
    return renderDevice.getHeight();
  }

  /**
   * Output the state of all root nodes (and the whole tree below) to a String. This is meant to aid in debugging.
   * DON'T RELY ON ANY INFORMATION IN HERE SINCE THIS CAN BE CHANGED IN FUTURE RELEASES!
   *
   * @return String that contains the debugging info for all root nodes
   */
  public String getSceneInfoLog() {
    return getSceneInfoLog("(?s).*");
  }

  /**
   * Output the state of all root nodes (and the whole tree below) to a String. This is meant to aid in debugging.
   * DON'T RELY ON ANY INFORMATION IN HERE SINCE THIS CAN BE CHANGED IN FUTURE RELEASES!
   *
   * @param filter regexp to filter the output (Example: "position" will only output position info)
   * @return String that contains the debugging info for all root nodes
   */
  public String getSceneInfoLog(final String filter) {
    StringBuilder result = new StringBuilder("Nifty scene info log\n");
    for (int i=0; i<rootNodes.size(); i++) {
      rootNodes.get(i).getStateInfo(result, filter);
    }
    return result.toString();
  }

  // Internal methods

  private NiftyNode createRootNodeInternal() {
    return InternalNiftyNode.newRootNode(this, InternalNiftyIdGenerator.generate(), rootNodePlacementLayout);
  }
}
