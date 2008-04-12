package de.lessvoid.nifty.layout;

/**
 * LayoutPart is a composition of Box and BoxConstraints.
 * @author void
 */
public class LayoutPart {

  /**
   * the box.
   */
  private Box box;

  /**
   * the box constraints.
   */
  private BoxConstraints boxConstraints;

  /**
   * Create a new instance.
   */
  public LayoutPart() {
    this.box = new Box();
    this.boxConstraints = new BoxConstraints();
  }

  /**
   * Create a new LayoutPart instance.
   * @param newBox the new box
   * @param newBoxConstraints the new box constraints
   */
  public LayoutPart(final Box newBox, final BoxConstraints newBoxConstraints) {
    this.box = newBox;
    this.boxConstraints = newBoxConstraints;
  }

  /**
   * Get the box of this LayoutPart.
   * @return the box
   */
  public final Box getBox() {
    return box;
  }

  /**
   * Get the box constraints for this LayoutPart.
   * @return the box Constraints
   */
  public final BoxConstraints getBoxConstraints() {
    return boxConstraints;
  }
}
