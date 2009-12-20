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
    if (isInvalid(root, children)) {
      return;
    }

    int rootBoxX = getRootBoxX(root);
    int rootBoxY = getRootBoxY(root);
    int rootBoxWidth = getRootBoxWidth(root);
    int rootBoxHeight = getRootBoxHeight(root);

    int x = rootBoxX;
    for (int i = 0; i < children.size(); i++) {
      LayoutPart current = children.get(i);
      Box box = current.getBox();
      BoxConstraints boxConstraints = current.getBoxConstraints();

      box.setHeight(processHeightConstraint(rootBoxHeight, box, boxConstraints));
      box.setY(processVerticalAlignment(rootBoxY, rootBoxHeight, box, boxConstraints));
      box.setX(x);

      int elementWidth = calcElementWidth(children, rootBoxWidth, boxConstraints);
      box.setWidth(elementWidth);

      x += elementWidth;
    }
  }

  private int processHeightConstraint(final int rootBoxHeight, final Box box, final BoxConstraints constraint) {
    if (constraint.getHeight() != null) {
      return constraint.getHeight().getValueAsInt(rootBoxHeight);
    } else {
      return rootBoxHeight;
    }
  }

  private int processVerticalAlignment(final int rootBoxY, final int rootBoxHeight, final Box box, final BoxConstraints boxConstraints) {
    if (VerticalAlign.center.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY + ((rootBoxHeight - box.getHeight()) / 2);
    } else if (VerticalAlign.top.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY;
    } else if (VerticalAlign.bottom.equals(boxConstraints.getVerticalAlign())) {
      return rootBoxY + (rootBoxHeight - box.getHeight());
    } else {
      return rootBoxY;
    }
  }

  private int calcElementWidth(final List < LayoutPart > children, final int rootBoxWidth, final BoxConstraints boxConstraints) {
    if (boxConstraints.getWidth() != null) {
      int h = (int) boxConstraints.getWidth().getValue(rootBoxWidth);
      if (h != -1) {
        return h;
      }
    }
    return getMaxNonFixedWidth(children, rootBoxWidth);
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

  private boolean isInvalid(final LayoutPart root, final List <LayoutPart> children) {
    return root == null || children == null || children.size() == 0;
  }

  private int getRootBoxX(final LayoutPart root) {
    return root.getBox().getX() + root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth());
  }

  private int getRootBoxY(final LayoutPart root) {
    return root.getBox().getY() + root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight());
  }

  private int getRootBoxWidth(final LayoutPart root) {
    return root.getBox().getWidth() - root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth()) - root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getBox().getWidth());
  }

  private int getRootBoxHeight(final LayoutPart root) {
    return root.getBox().getHeight() - root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight()) - root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight());
  }
}
