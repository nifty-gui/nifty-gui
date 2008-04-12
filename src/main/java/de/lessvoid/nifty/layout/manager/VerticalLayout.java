package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * A VerticalLayout implementation of the LayoutManager interface.
 * The children elements are arranged in a vertical form
 * in relation to the root element.
 *
 * @author void
 */
public class VerticalLayout implements LayoutManager {

  /**
   * Layout the elements.
   *
   * @param root the root element
   * @param children the children
   */
  public final void layoutElements(
      final LayoutPart root,
      final List < LayoutPart > children) {

    // make the parameters any sense?
    if (root == null || children == null || children.size() == 0) {
      return;
    }

    // now do the layout
    Box rootBox = root.getBox();

    int y = rootBox.getY();
    for (int i = 0; i < children.size(); i++) {
      LayoutPart current = children.get(i);
      Box box = current.getBox();
      BoxConstraints boxConstraints = current.getBoxConstraints();

      // first constraint the element width
      processWidthConstaints(rootBox, box, boxConstraints);

      // second align it
      processHorizontalAlignment(rootBox, box, boxConstraints);

      box.setY(y);

      int elementHeight = calcElementHeight(children, rootBox, boxConstraints);
      box.setHeight(elementHeight);
      y += elementHeight;
    }
  }

  /**
   * @param rootBox the rootBox
   * @param box the current box
   * @param constraints the constraints of the current box
   */
  private void processWidthConstaints(
      final Box rootBox,
      final Box box,
      final BoxConstraints constraints) {
    if (constraints != null && constraints.getWidth() != null) {
      box.setWidth((int) constraints.getWidth().getValue(rootBox.getWidth()));
    } else {
      box.setWidth(rootBox.getWidth());
    }
  }

  /**
   * @param rootBox the rootBox
   * @param box the current box
   * @param constraints the constraints of the current box
   */
  private void processHorizontalAlignment(
      final Box rootBox,
      final Box box,
      final BoxConstraints constraints) {
    if (HorizontalAlign.center.equals(constraints.getHorizontalAlign())) {
      box.setX(rootBox.getX() + ((rootBox.getWidth() - box.getWidth()) / 2));
    } else if (HorizontalAlign.right.equals(constraints.getHorizontalAlign())) {
      box.setX(rootBox.getX() + (rootBox.getWidth() - box.getWidth()));
    } else if (HorizontalAlign.left.equals(constraints.getHorizontalAlign())) {
      box.setX(rootBox.getX());
    } else {
      box.setX(rootBox.getX());
    }
  }

  /**
   * @param children the children elements
   * @param rootBox the rootbox
   * @param boxConstraints the current box constraints
   * @return the element height
   */
  private int calcElementHeight(
      final List < LayoutPart > children,
      final Box rootBox,
      final BoxConstraints boxConstraints) {
    int elementHeight;
    if (boxConstraints != null && boxConstraints.getHeight() != null) {
      int h = (int) boxConstraints.getHeight().getValue(rootBox.getHeight());
      if (h == -1) {
        h = getMaxNonFixedHeight(children, rootBox.getHeight());
      }
      elementHeight = h;
    } else {
      elementHeight = getMaxNonFixedHeight(children, rootBox.getHeight());
    }
    return elementHeight;
  }

  /**
   * Calculates maximum non fixed height of the given elements.
   * @param elements the elements
   * @param parentHeight the parent height
   * @return the calculated max height
   */
  private int getMaxNonFixedHeight(
      final List < LayoutPart > elements,
      final int parentHeight) {
    int maxFixedHeight = 0;
    int fixedCount = 0;

    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      BoxConstraints original = p.getBoxConstraints();
      if (original != null && original.getHeight() != null) {
        if (original.getHeight().isPercentOrPixel()) {
          maxFixedHeight += original.getHeight().getValue(parentHeight);
          fixedCount++;
        }
      }
    }

    int notFixedCount = elements.size() - fixedCount;
    if (notFixedCount > 0) {
      return (parentHeight - maxFixedHeight) / notFixedCount;
    } else {
      return (parentHeight - maxFixedHeight);
    }
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public final SizeValue calculateConstraintWidth(final List < LayoutPart > children) {
    return null;
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public final SizeValue calculateConstraintHeight(final List < LayoutPart > children) {
    int newHeight = 0;
    for (LayoutPart e : children) {
      newHeight += e.getBoxConstraints().getHeight().getValueAsInt(0);
    }
    return new SizeValue(newHeight + "px");
  }
}
