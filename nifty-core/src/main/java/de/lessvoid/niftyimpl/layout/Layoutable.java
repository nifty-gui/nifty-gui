package de.lessvoid.niftyimpl.layout;


/**
 * Layoutable is the interface used in all layout related processes.
 * @author void
 */
public interface Layoutable {

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
}
