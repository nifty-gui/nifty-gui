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
   * copy constructor.
   * @param src source
   */
  public LayoutPart(final LayoutPart src) {
    this.box = new Box(src.getBox());
    this.boxConstraints = new BoxConstraints(src.getBoxConstraints());
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

  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("box [" + box.getX() + ", " + box.getY() + ", " + box.getWidth() + ", " + box.getHeight() + "] with constraints [" + boxConstraints.getX() + ", " + boxConstraints.getY() + ", " + boxConstraints.getWidth() + ", " + boxConstraints.getHeight() + "]");
    return result.toString();
  }
}
