package de.lessvoid.nifty.internal.layout;

import java.util.List;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

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
/* package */ class LayoutCenter implements Layout {

  @Override
  public void layoutElements(final Layoutable rootElement, final List <Layoutable> elements) {
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    Box rootBox = rootElement.getLayoutPos();
    BoxConstraints rootBoxConstraints = rootElement.getBoxConstraints();

    for (int i=0; i<elements.size(); i++) {
      layoutElement(elements.get(i), rootBox, rootBoxConstraints);
    }
  }

  private int leftMargin(final BoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
  }

  private int rightMargin(final BoxConstraints boxConstraints, final int rootBoxWidth) {
    return boxConstraints.getMarginRight().getValueAsInt(rootBoxWidth);
  }

  private int topMargin(final BoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginTop().getValueAsInt(rootBoxHeight);
  }

  private int bottomMargin(final BoxConstraints boxConstraints, final int rootBoxHeight) {
    return boxConstraints.getMarginBottom().getValueAsInt(rootBoxHeight);
  }

  private void layoutElement(final Layoutable element, Box rootBox, BoxConstraints rootBoxConstraints) {
    Box box = element.getLayoutPos();
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

    box.setX(box.getX() + leftMargin(constraint, rootBox.getWidth()) - rightMargin(constraint, rootBox.getWidth()));
    box.setY(box.getY() + topMargin(constraint, rootBox.getHeight()) - bottomMargin(constraint, rootBox.getHeight()));
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
    int rootBoxWidth = rootBox.getWidth() -
                       rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth()) -
                       rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth());

    int boxWidth = (int) constraint.getWidth().getValue(rootBoxWidth);
    if (constraint.getWidth().hasHeightSuffix()) {
      boxWidth = (int) constraint.getWidth().getValue(box.getHeight()); 
    }
    box.setWidth(boxWidth);

    if (constraint.getHorizontalAlign() == HorizontalAlignment.left) {
      box.setX(rootBoxX);
    } else if (constraint.getHorizontalAlign() == HorizontalAlignment.right) {
      box.setX(rootBoxX +
               rootBox.getWidth() -
               rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()) - boxWidth);
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
    int rootBoxHeight = rootBox.getHeight() -
                        rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight()) -
                        rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight());

    int boxHeight = (int) constraint.getHeight().getValue(rootBoxHeight);
    if (constraint.getHeight().hasWidthSuffix()) {
      boxHeight = (int) constraint.getHeight().getValue(box.getWidth());
    }
    box.setHeight(boxHeight);

    if (constraint.getVerticalAlign() == VerticalAlignment.top) {
      box.setY(rootBoxY);
    } else if (constraint.getVerticalAlign() == VerticalAlignment.bottom) {
      box.setY(rootBoxY +
               rootBox.getHeight() -
               rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight()) - boxHeight);
    } else {
      // center is default in here
      box.setY(rootBoxY + (rootBoxHeight - boxHeight) / 2);
    }
  }
}
