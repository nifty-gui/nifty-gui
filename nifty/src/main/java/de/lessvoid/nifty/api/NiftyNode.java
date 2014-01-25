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
    this.impl.setNiftyNode(this);
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
   * Set the x constraint of this NiftyNode. Please remember that not all layouts will respect that value.
   * @param value the UnitValue representing the new x position
   */
  public void setXConstraint(final UnitValue value) {
    impl.setXConstraint(value);
  }

  /**
   * Set the y constraint of this NiftyNode. Please remember that not all layouts will respect that value.
   * @param value the UnitValue representing the new y position
   */
  public void setYConstraint(final UnitValue value) {
    impl.setYConstraint(value);
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

  /**
   * Get the current background color of this node.
   *
   * @return the currently set background color
   */
  public NiftyColor getBackgroundColor() {
    return impl.getBackgroundColor();
  }

  /**
   * Set the pivot point that will form the base of all calculation. You can think of this as the center. This is in
   * the interval [0.0, 1.0].
   *
   * @param x x
   * @param y y
   */
  public void setPivot(final double x, final double y) {
    impl.setPivot(x, y);
  }

  /**
   * Set rotation around the x-axis in degrees.
   * @param angle rotation angle
   */
  public void setRotationX(final double angle) {
    impl.setRotationX(angle);
  }

  /**
   * Set rotation around the y-axis in degrees.
   * @param angle rotation angle
   */
  public void setRotationY(final double angle) {
    impl.setRotationY(angle);
  }

  /**
   * Set rotation around the z-axis in degrees.
   * @param angle rotation angle
   */
  public void setRotationZ(final double angle) {
    impl.setRotationZ(angle);
  }

  /**
   * Set scale for x-axis.
   * @param factor factor
   */
  public void setScaleX(final double factor) {
    impl.setScaleX(factor);
  }

  /**
   * Set scale for y-axis.
   * @param factor factor
   */
  public void setScaleY(final double factor) {
    impl.setScaleY(factor);
  }

  /**
   * Set scale for z-axis.
   * @param factor factor
   */
  public void setScaleZ(final double factor) {
    impl.setScaleZ(factor);
  }

  /**
   * Get rotation around the x-axis in degrees.
   * @return angle rotation angle
   */
  public double getRotationX() {
    return impl.getRotationX();
  }

  /**
   * Set rotation around the y-axis in degrees.
   * @return angle rotation angle
   */
  public double getRotationY() {
    return impl.getRotationY();
  }

  /**
   * Get rotation around the z-axis in degrees.
   * @return angle rotation angle
   */
  public double getRotationZ() {
    return impl.getRotationZ();
  }

  /**
   * Get scale for x-axis.
   * @return x scale
   */
  public double getScaleX() {
    return impl.getScaleX();
  }

  /**
   * Get scale for y-axis.
   * @return y scale
   */
  public double getScaleY() {
    return impl.getScaleY();
  }

  /**
   * Get scale for z-axis.
   * @return z scale
   */
  public double getScaleZ() {
    return impl.getScaleZ();
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
   * Forces this Node to be redrawn the next time nodes are refreshed.
   */
  public void requestRedraw() {
    impl.requestRedraw();
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

  // package private accessor stuff

  InternalNiftyNode getImpl() {
    return impl;
  }

  static {
    NiftyNodeAccessor.DEFAULT = new InternalNiftyNodeAccessorImpl();
  }

  static NiftyNode newInstance(final InternalNiftyNode impl) {
    return new NiftyNode(impl);
  }
}
