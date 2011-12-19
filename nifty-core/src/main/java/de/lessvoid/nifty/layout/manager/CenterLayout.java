package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * CenterLayout centers all child elements. If there are
 * more than one child elements all elements will be
 * centered (and over layed above each other).
 * 
 * Remember that center probably makes only sense if the
 * centered element has some width and height constraints set.
 *
 * @author void
 */
public class CenterLayout implements LayoutManager {
  /**
   * layoutElements.
   * @param rootElement @see {@link LayoutManager}
   * @param elements @see {@link LayoutManager}
   */
  public void layoutElements(final LayoutPart rootElement, final List < LayoutPart > elements) {

    // check for useful params
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    Box rootBox = rootElement.getBox();
    BoxConstraints rootBoxConstraints = rootElement.getBoxConstraints();

    for (int i=0; i<elements.size(); i++) {
      layoutElement(elements.get(i), rootBox, rootBoxConstraints);
    }
  }

  private void layoutElement(final LayoutPart element, Box rootBox, BoxConstraints rootBoxConstraints) {
    Box box = element.getBox();
    BoxConstraints constraint = element.getBoxConstraints();

    if (constraint.getWidth() != null && constraint.getWidth().hasHeightSuffix()) {
      handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
      handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    } else if (constraint.getHeight() != null && constraint.getHeight().hasWidthSuffix()) {
      handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
      handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    } else {
      handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
      handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    }
  }

  void handleHorizontalAlignment(
      final Box rootBox,
      final BoxConstraints rootBoxConstraints,
      final Box box,
      final BoxConstraints constraint) {
    if (constraint.getWidth() != null) {
      handleWidthConstraint(rootBox, rootBoxConstraints, box, constraint);
    } else {
      box.setX(
          rootBox.getX()
          + rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth()));
      box.setWidth(
          rootBox.getWidth()
          - rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth())
          - rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()));
    }
  }

  void handleVerticalAlignment(
      final Box rootBox,
      final BoxConstraints rootBoxConstraints,
      final Box box,
      final BoxConstraints constraint) {
    if (constraint.getHeight() != null) {
      handleHeightConstraint(rootBox, rootBoxConstraints, box, constraint);
    } else {
      box.setY(
          rootBox.getY()
          + rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight()));
      box.setHeight(
          rootBox.getHeight()
          - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight())
          - rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight()));
    }
  }

  private void handleWidthConstraint(
      final Box rootBox,
      final BoxConstraints rootBoxConstraints,
      final Box box,
      final BoxConstraints constraint) {
    int rootBoxX = rootBox.getX() + rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth());
    int rootBoxWidth = rootBox.getWidth() - rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth()) - rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth());

    int boxWidth = (int) constraint.getWidth().getValue(rootBoxWidth);
    if (constraint.getWidth().hasHeightSuffix()) {
      boxWidth = (int) constraint.getWidth().getValue(box.getHeight()); 
    }
    box.setWidth(boxWidth);

    if (constraint.getHorizontalAlign() == HorizontalAlign.left) {
      box.setX(rootBoxX);
    } else if (constraint.getHorizontalAlign() == HorizontalAlign.right) {
      box.setX(rootBoxX + rootBox.getWidth() - rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()) - boxWidth);
    } else {
      // default and center is the same in here
      box.setX(rootBoxX + (rootBoxWidth - boxWidth) / 2);
    }
  }

  private void handleHeightConstraint(
      final Box rootBox,
      final BoxConstraints rootBoxConstraints,
      final Box box,
      final BoxConstraints constraint) {
    int rootBoxY = rootBox.getY() + rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight());
    int rootBoxHeight = rootBox.getHeight() - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight()) - rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight());

    int boxHeight = (int) constraint.getHeight().getValue(rootBoxHeight);
    if (constraint.getHeight().hasWidthSuffix()) {
      boxHeight = (int) constraint.getHeight().getValue(box.getWidth());
    }
    box.setHeight(boxHeight);

    if (constraint.getVerticalAlign() == VerticalAlign.top) {
      box.setY(rootBoxY);
    } else if (constraint.getVerticalAlign() == VerticalAlign.bottom) {
      box.setY(rootBoxY + rootBox.getHeight() - rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight()) - boxHeight);
    } else {
      // center is default in here
      box.setY(rootBoxY + (rootBoxHeight - boxHeight) / 2);
    }
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public SizeValue calculateConstraintWidth(final LayoutPart root, final List < LayoutPart > children) {
    if (children.isEmpty()) {
      return null;
    }
    LayoutPart firstChild = children.get(0);
    if (firstChild == null) {
      return null;
    }
    BoxConstraints constraint = firstChild.getBoxConstraints();
    if (constraint == null) {
      return null;
    }
    return
      new SizeValue(
          constraint.getWidth().getValueAsInt(0) +
          root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth()) +
          root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getBox().getWidth()) + "px");
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public SizeValue calculateConstraintHeight(final LayoutPart root, final List < LayoutPart > children) {
    if (children.isEmpty()) {
      return null;
    }
    LayoutPart firstChild = children.get(0);
    if (firstChild == null) {
      return null;
    }
    BoxConstraints constraint = firstChild.getBoxConstraints();
    if (constraint == null) {
      return null;
    }
    return
      new SizeValue(
          constraint.getHeight().getValueAsInt(0) +
          root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight()) +
          root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight()) + "px");

  }
}
