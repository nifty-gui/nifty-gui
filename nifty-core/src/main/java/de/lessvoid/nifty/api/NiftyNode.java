package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.InternalLayout;
import de.lessvoid.nifty.internal.InternalLayoutAbsolute;
import de.lessvoid.nifty.internal.InternalLayoutAbsolute.KeepInsidePostProcess;
import de.lessvoid.nifty.internal.InternalLayoutCenter;
import de.lessvoid.nifty.internal.InternalLayoutHorizontal;
import de.lessvoid.nifty.internal.InternalLayoutOverlay;
import de.lessvoid.nifty.internal.InternalLayoutVertical;

/**
 * The core element of the Nifty scene graph is a NiftyNode. It is created by
 * the main Nifty instance and represents a hierarchical structure of a Nifty
 * GUI.
 * 
 * @author void
 */
public interface NiftyNode {

  /**
   * Get the x position of this node.
   * 
   * @return x position of this node.
   */
  int getX();

  /**
   * Get the y position of this node.
   * 
   * @return y y position of this node.
   */
  int getY();

  /**
   * Get the width of this node.
   * 
   * @return width width of this element.
   */
  int getWidth();

  /**
   * Get the height of this node.
   * 
   * @return height height of this node.
   */
  int getHeight();

  /**
   * Set the horizontal alignment.
   * 
   * @param alignment
   *          the new horizontal alignment
   */
  void setHorizontalAlignment(HorizontalAlignment alignment);

  /**
   * Set the vertical alignment.
   * 
   * @param alignment
   *          the new vertical alignment
   */
  void setVerticalAlignment(VerticalAlignment alignment);

  /**
   * Change the width constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new width
   */
  void setWidthConstraint(UnitValue px);

  /**
   * Change the height constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new height
   */
  void setHeightConstraint(UnitValue px);

  /**
   * Set the ChildLayout for this NiftyNode. The ChildLayout defines the way
   * child element of this node will be layed out.
   * 
   * @param childLayout
   */
  void setChildLayout(ChildLayout childLayout);

  /**
   * Create a new NiftyNode and make this node it's parent.
   * 
   * @return a new NiftyNode
   */
  NiftyNode createChildNode();

  /**
   * Create a new NiftyNode and make this node it's parent. Use the given
   * childLayout for the new child node.
   * 
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  NiftyNode createChildNode(ChildLayout childLayout);

  /**
   * Create a new NiftyNode and make this node it's parent. The new ChildNode
   * will be given the given width and height. This is a convienience method
   * only since you can always change the height of the node after you've
   * created a new node.
   * 
   * @param width
   *          width of the new NiftyNode
   * @param height
   *          height of the new NiftyNode
   * @return a new NiftyNode
   */
  NiftyNode createChildNode(UnitValue width, UnitValue height);

  /**
   * Create a new NiftyNode and make this node it's parent. The new ChildNode
   * will be given the given width and height. This is a convienience method
   * only since you can always change the height of the node after you've
   * created a new node.
   * 
   * @param width
   *          width of the new NiftyNode
   * @param height
   *          height of the new NiftyNode
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  NiftyNode createChildNode(UnitValue width, UnitValue height, ChildLayout childLayout);

  /**
   * Collect state information for this node (and all of it's children) into the
   * given StringBuilder.
   * 
   * Please note:
   * 
   * The data written is highly fragile and can be changed between releases
   * without any warning. Don't rely on this information other than for
   * debugging the state of the nodes.
   * 
   * @param result
   *          the StringBuilder to add the state info to
   */
  void getStateInfo(final StringBuilder result);

  /**
   * @see #getStateInfo(StringBuilder)
   * 
   *      Additionally this methods allow you to filter the result with a
   *      regular expression. You'll always get the id of the elements back and
   *      the rows that match your regular expression.
   * 
   *      Example:
   * 
   *      <pre>
   * niftyNode.getStateInfo(result, &quot;.*position.*&quot;); // will only give you position
   * // information
   * </pre>
   * 
   * @param result
   *          the StringBuilder to add the state info to
   * @param pattern
   *          the pattern to match the output
   */
  void getStateInfo(final StringBuilder result, final String pattern);
  
  /**
   * The ChildLayout enumeration will define how this NiftyNode will layout its
   * children.
   * 
   * @author void
   */
  public enum ChildLayout {
    /**
     * Absolute doesn't really layout the child nodes. It just absolute
     * positions them according to their constraints.
     */
    Absolute(new InternalLayoutAbsolute()),

    /**
     * This works the same as Absolute but automatically ensures that child
     * elements are not positioned outside of the their parent node. If
     * necessary the constrained positions will be automatically corrected to
     * force the child elements inside of the parent.
     */
    AbsoluteInside(new InternalLayoutAbsolute(new KeepInsidePostProcess())),

    /**
     * CenterLayout centers all child elements. If there are more than one child
     * elements all elements will be centered (and will overlay each other
     * consequently). Remember that center probably makes only sense if the
     * centered element has some width and height constraints set.
     */
    Center(new InternalLayoutCenter()),

    /**
     * The child nodes are arranged horizontally in relation to the root
     * element.
     */
    Horizontal(new InternalLayoutHorizontal()),

    /**
     * OverlayLayout doesn't layout things. It just forwards the size of the
     * root box to the children.
     */
    Overlay(new InternalLayoutOverlay()),

    /**
     * The child nodes are arranged vertically in relation to the root element.
     */
    Vertical(new InternalLayoutVertical());

    private InternalLayout layout;

    private ChildLayout(final InternalLayout layout) {
      this.layout = layout;
    }

    public InternalLayout getLayout() {
      return layout;
    }
  }
}
