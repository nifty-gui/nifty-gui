package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * A HorizontalLayout implementation of the LayoutManager interface.
 * The children elements are arranged in a horizontal form
 * in relation to the root element.
 *
 * @author void
 */
public class HorizontalLayout implements LayoutManager {

  /**
   * Layout the elements.
   *
   * @param root the root element
   * @param children the children
   */
  public final void layoutElements(final LayoutPart root, final List < LayoutPart > children) {

    // check for useful input data
    if (root == null || children == null || children.size() == 0) {
      return;
    }

    // now do the layout
    Box rootBox = root.getBox();
    int x = rootBox.getX();

    for (int i = 0; i < children.size(); i++) {
      LayoutPart current = children.get(i);
      Box box = current.getBox();
      BoxConstraints boxConstraints = current.getBoxConstraints();

      // process height constraint
      processHeightConstraint(rootBox, box, boxConstraints);

      // process vertical alignment
      processVerticalAlignment(rootBox, box, boxConstraints);

      box.setX(x);

      int elementWidth = calcElementWidth(children, rootBox, boxConstraints);
      box.setWidth(elementWidth);
      x += elementWidth;
    }
  }

  /**
   * @param rootBox the rootBox
   * @param box the box to change
   * @param constraint the constraints to check
   */
  private void processHeightConstraint(
      final Box rootBox,
      final Box box,
      final BoxConstraints constraint) {
    if (constraint.getHeight() != null) {
      box.setHeight((int) constraint.getHeight().getValue(rootBox.getHeight()));
    } else {
      box.setHeight(rootBox.getHeight());
    }
  }

  /**
   * @param rootBox the rootBox
   * @param box the box to change
   * @param boxConstraints constraints the constraints to check
   */
  private void processVerticalAlignment(
      final Box rootBox,
      final Box box,
      final BoxConstraints boxConstraints) {
    if (VerticalAlign.center.equals(boxConstraints.getVerticalAlign())) {
      box.setY(rootBox.getY() + ((rootBox.getHeight() - box.getHeight()) / 2));
    } else if (VerticalAlign.top.equals(boxConstraints.getVerticalAlign())) {
      box.setY(rootBox.getY());
    } else if (VerticalAlign.bottom.equals(boxConstraints.getVerticalAlign())) {
      box.setY(rootBox.getY() + (rootBox.getHeight() - box.getHeight()));
    } else {
      box.setY(rootBox.getY());
    }
  }

  /**
   * @param children the children
   * @param rootBox the rootBox
   * @param boxConstraints the box constraints
   * @return the element width
   */
  private int calcElementWidth(
      final List < LayoutPart > children,
      final Box rootBox,
      final BoxConstraints boxConstraints) {
    int elementWidth;
    if (boxConstraints.getWidth() != null) {
      int h = (int) boxConstraints.getWidth().getValue(rootBox.getWidth());
      if (h == -1) {
        h = getMaxNonFixedWidth(children, rootBox.getWidth());
      }
      elementWidth = h;
    } else {
      elementWidth = getMaxNonFixedWidth(children, rootBox.getWidth());
    }
    return elementWidth;
  }

  /**
   *
   * @param elements the child elements the max width is going
   * to be calculated
   * @param parentWidth the width of the parent element
   * @return max non fixed width
   */
  private int getMaxNonFixedWidth(
      final List < LayoutPart > elements,
      final int parentWidth
      ) {
    int maxFixedWidth = 0;
    int fixedCount = 0;
    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      BoxConstraints original = p.getBoxConstraints();

      if (original.getWidth() != null) {
        if (original.getWidth().isPercentOrPixel()) {
          maxFixedWidth += original.getWidth().getValue(parentWidth);
          fixedCount++;
        }
      }
    }

    int notFixedCount = elements.size() - fixedCount;
    if (notFixedCount > 0) {
      return (parentWidth - maxFixedWidth) / notFixedCount;
    } else {
      return (parentWidth - maxFixedWidth);
    }
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public final SizeValue calculateConstraintWidth(final LayoutPart root, final List < LayoutPart > children) {
    return null;
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public final SizeValue calculateConstraintHeight(final LayoutPart root, final List < LayoutPart > children) {
    int newHeight = 0;
    for (LayoutPart e : children) {
      int partHeight = e.getBoxConstraints().getHeight().getValueAsInt(0)
        - e.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight())
        - e.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight());
      if (partHeight > newHeight) {
        newHeight = partHeight;
      }
    }
    return new SizeValue(newHeight + "px");
  }
}
