package de.lessvoid.niftyimpl.layout;

import de.lessvoid.nifty.layout.SizeValue;

/**
 * LayoutPart is the interface used in all layout related processes.
 * @author void
 */
public interface LayoutPart {

  /**
   * Get the box of this LayoutPart.
   * @return the box
   */
  Box getLayoutPos();

  /**
   * Get the box constraints for this LayoutPart.
   * @return the box Constraints
   */
  BoxConstraints getBoxConstraints();

  /**
   * Returns the maximum width of all child elements. This takes padding of this LayoutPart as well as the margin
   * values of the child elements into account.
   *
   * @return max width
   */
  SizeValue getMaxChildWidth();

  /**
   * Returns the maximum height of the given child elements. This takes padding of this LayoutPart as well as the
   * margin values of the child elements into account.
   *
   * @return max height
   */
  SizeValue getMaxChildHeight();

  /**
   * Calculates the sum of the width of all children. Taking padding of this and margin of child elements into account.
   *
   * @return width sum
   */
  SizeValue getTotalChildrenWidth();

  /**
   * Calculates the sum of the height of all children. Taking padding of this and margin of child elements into account.
   *
   * @return width sum
   */
  SizeValue getTotalChildrenHeight();

}
