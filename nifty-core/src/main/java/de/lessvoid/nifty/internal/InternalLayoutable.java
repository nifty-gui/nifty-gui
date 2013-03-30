package de.lessvoid.nifty.internal;



/**
 * InternalLayoutable is the interface used in all layout related processes.
 * @author void
 */
public interface InternalLayoutable {

  /**
   * Get the box of this LayoutPart.
   * @return the box
   */
  InternalBox getLayoutPos();

  /**
   * Get the box constraints for this LayoutPart.
   * @return the box Constraints
   */
  InternalBoxConstraints getBoxConstraints();
}
