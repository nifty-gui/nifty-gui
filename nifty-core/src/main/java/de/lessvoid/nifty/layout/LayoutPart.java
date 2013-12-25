package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * LayoutPart is a composition of Box and BoxConstraints.
 *
 * @author void
 */
public class LayoutPart {
  /**
   * the box.
   */
  @Nonnull
  private final Box box;

  /**
   * the box constraints.
   */
  @Nonnull
  private final BoxConstraints boxConstraints;

  /**
   * Create a new instance.
   */
  public LayoutPart() {
    this.box = new Box();
    this.boxConstraints = new BoxConstraints();
  }

  /**
   * Create a new LayoutPart instance.
   *
   * @param newBox            the new box
   * @param newBoxConstraints the new box constraints
   */
  public LayoutPart(@Nonnull final Box newBox, @Nonnull final BoxConstraints newBoxConstraints) {
    this.box = newBox;
    this.boxConstraints = newBoxConstraints;
  }

  /**
   * copy constructor.
   *
   * @param src source
   */
  public LayoutPart(@Nonnull final LayoutPart src) {
    this.box = new Box(src.getBox());
    this.boxConstraints = new BoxConstraints(src.getBoxConstraints());
  }

  /**
   * Get the box of this LayoutPart.
   *
   * @return the box
   */
  @Nonnull
  public final Box getBox() {
    return box;
  }

  /**
   * Get the box constraints for this LayoutPart.
   *
   * @return the box Constraints
   */
  @Nonnull
  public final BoxConstraints getBoxConstraints() {
    return boxConstraints;
  }

  @Override
  @Nonnull
  public String toString() {
    return "box [" + box.getX() + ", " + box.getY() + ", " +
        "" + box.getWidth() + ", " + box.getHeight() + "] with constraints [" + boxConstraints.getX() + ", " +
        "" + boxConstraints.getY() + ", " +
        "" + boxConstraints.getWidth() + ", " + boxConstraints.getHeight() + "]";
  }

  /**
   * Calculates the maximum width of the given child elements. This takes padding of this LayoutPart
   * as well as the margin values of the child elements into account.
   *
   * @param children List<LayoutPart> to calculate max width
   * @return max width
   */
  @Nonnull
  public SizeValue getMaxWidth(@Nonnull final List<LayoutPart> children) {
    int newWidth = 0;
    for (LayoutPart e : children) {
      int partWidth = e.getBoxConstraints().getWidth().getValueAsInt(0);
      partWidth += e.getBoxConstraints().getMarginLeft().getValueAsInt(0);
      partWidth += e.getBoxConstraints().getMarginRight().getValueAsInt(0);
      if (partWidth > newWidth) {
        newWidth = partWidth;
      }
    }
    return SizeValue.px(newWidth);
  }

  /**
   * Calculates the maximum height of the given child elements. This takes padding of this LayoutPart
   * as well as the margin values of the child elements into account.
   *
   * @param children List<LayoutPart> to calculate max height
   * @return max height
   */
  @Nonnull
  public SizeValue getMaxHeight(@Nonnull final List<LayoutPart> children) {
    int newHeight = 0;
    for (LayoutPart e : children) {
      int partHeight = e.getBoxConstraints().getHeight().getValueAsInt(0);
      partHeight += e.getBoxConstraints().getMarginTop().getValueAsInt(0);
      partHeight += e.getBoxConstraints().getMarginBottom().getValueAsInt(0);
      if (partHeight > newHeight) {
        newHeight = partHeight;
      }
    }
    return SizeValue.px(newHeight);
  }

  /**
   * Calculates the sum of the width of all children. Taking padding of this and margin of child
   * elements into account.
   *
   * @param children List<LayoutPart> to calculate width sum
   * @return width sum
   */
  @Nonnull
  public SizeValue getSumWidth(@Nonnull final List<LayoutPart> children) {
    int newWidth = 0;
    for (LayoutPart e : children) {
      newWidth += e.getBoxConstraints().getMarginLeft().getValueAsInt(0);
      newWidth += e.getBoxConstraints().getWidth().getValueAsInt(0);
      newWidth += e.getBoxConstraints().getMarginRight().getValueAsInt(0);
    }
    return SizeValue.px(newWidth);
  }

  /**
   * Calculates the sum of the height of all children. Taking padding of this and margin of child
   * elements into account.
   *
   * @param children List<LayoutPart> to calculate height sum
   * @return width sum
   */
  @Nonnull
  public SizeValue getSumHeight(@Nonnull final List<LayoutPart> children) {
    int newHeight = 0;
    for (LayoutPart e : children) {
      newHeight += e.getBoxConstraints().getHeight().getValueAsInt(0);
      newHeight += e.getBoxConstraints().getMarginTop().getValueAsInt(0);
      newHeight += e.getBoxConstraints().getMarginBottom().getValueAsInt(0);
    }
    return SizeValue.px(newHeight);
  }
}
