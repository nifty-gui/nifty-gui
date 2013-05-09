package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.layout.InternalLayout;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;
import de.lessvoid.nifty.internal.layout.InternalLayoutHorizontal;
import de.lessvoid.nifty.internal.layout.InternalLayoutNone;
import de.lessvoid.nifty.internal.layout.InternalLayoutOverlay;
import de.lessvoid.nifty.internal.layout.InternalLayoutVertical;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute.KeepInsidePostProcess;
import de.lessvoid.nifty.internal.InternalNiftyNode;

/**
 * The core element of the Nifty scene graph is a NiftyNode. It is created by the main Nifty instance and represents the
 * hierarchical structure of a Nifty GUI.
 * 
 * @author void
 */
public class NiftyNode {
  private final InternalNiftyNode impl;

  /**
   * Please use one of the {@link Nifty#createRootNode()} methods to create a new NiftyNode. You're not supposed to
   * create an instance of this class directly and you're not supposed to extend from this class.
   */
  private NiftyNode(final InternalNiftyNode impl) {
    this.impl = impl;
  }

  InternalNiftyNode getImpl() {
    return impl;
  }

  static NiftyNode newInstance(final InternalNiftyNode impl) {
    return new NiftyNode(impl);
  }

  /**
   * Get the x position of this node.
   * 
   * @return x position of this node.
   */
  public int getX() {
    return impl.getX();
  }

  /**
   * Get the y position of this node.
   * 
   * @return y y position of this node.
   */
  public int getY() {
    return impl.getY();
  }

  /**
   * Get the width of this node.
   * 
   * @return width width of this element.
   */
  public int getWidth() {
    return impl.getWidth();
  }

  /**
   * Get the height of this node.
   * 
   * @return height height of this node.
   */
  public int getHeight() {
    return impl.getHeight();
  }

  /**
   * Set the horizontal alignment.
   * 
   * @param alignment
   *          the new horizontal alignment
   */
  public void setHorizontalAlignment(final HorizontalAlignment alignment) {
    impl.setHorizontalAlignment(alignment);
  }

  /**
   * Set the vertical alignment.
   * 
   * @param alignment
   *          the new vertical alignment
   */
  public void setVerticalAlignment(final VerticalAlignment alignment) {
    impl.setVerticalAlignment(alignment);
  }

  /**
   * Change the width constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new width
   */
  public void setWidthConstraint(final UnitValue value) {
    impl.setWidthConstraint(value);
  }

  /**
   * Change the height constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new height
   */
  public void setHeightConstraint(final UnitValue value) {
    impl.setHeightConstraint(value);
  }

  /**
   * Set the ChildLayout for this NiftyNode. The ChildLayout defines the way
   * child element of this node will be layed out.
   * 
   * @param childLayout
   */
  public void setChildLayout(final ChildLayout childLayout) {
    impl.setChildLayout(childLayout);
  }

  /**
   * Change the background color of this node to a new color. The default value is a fully transparent color.
   *
   * @param color the new background color
   */
  public void setBackgroundColor(final NiftyColor color) {
    impl.setBackgroundColor(color);
  }

  public void setRotation(final double angle) {
    impl.setRotation(angle);
  }

  /**
   * Set a NiftyCanvasPainter for the NiftyNode. This means you'd like to provide the content on your own. The
   * NiftyCanvasPainter is an interface you're supposed to implement. Nifty will call you back when it is time to
   * provide the content of this Node.
   *
   * @param painter the NiftyCanvasPainter instance to use for this Node
   */
  public void setContent(final NiftyCanvasPainter painter) {
    impl.setContent(painter);
  }

  /**
   * Create a new NiftyNode and make this node it's parent.
   * 
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode() {
    return new NiftyNode(impl.newChildNode());
  }

  /**
   * Create a new NiftyNode and make this node it's parent. Use the given
   * childLayout for the new child node.
   * 
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final ChildLayout childLayout) {
    return new NiftyNode(impl.newChildNode(childLayout));
  }

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
  public NiftyNode newChildNode(final UnitValue width, final UnitValue height) {
    return new NiftyNode(impl.newChildNode(width, height));
  }

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
  public NiftyNode newChildNode(final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    return new NiftyNode(impl.newChildNode(width, height, childLayout));
  }

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
  public void getStateInfo(final StringBuilder result) {
    impl.getStateInfo(result);
  }

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
  public void getStateInfo(final StringBuilder result, final String pattern) {
    impl.getStateInfo(result, pattern);
  }

  /**
   * Enable cache of this Node.
   */
  public void enableCache() {
    impl.setCache(true);
  }

  /**
   * Disable cache of this Node.
   */
  public void disableCache() {
    impl.setCache(false);
  }

  /**
   * The ChildLayout enumeration will define how this NiftyNode will layout its
   * children.
   * 
   * @author void
   */
  public enum ChildLayout {
    /**
     * A layout that does nothing. This is the default layout for a Node without any parents.
     */
    None(new InternalLayoutNone()),

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

  static {
    NiftyNodeAccessor.DEFAULT = new InternalNiftyNodeAccessorImpl();
  }
}
