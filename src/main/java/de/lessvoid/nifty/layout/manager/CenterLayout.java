package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * CenterLayout centers the very first child only in the
 * given root element. Remember that center probably makes
 * only sense if the centered element has some width and
 * height constraints set.
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

    // we only support center of the very first element
    Box rootBox = rootElement.getBox();
    BoxConstraints rootBoxConstraints = rootElement.getBoxConstraints();

    Box box = elements.get(0).getBox();
    BoxConstraints constraint = elements.get(0).getBoxConstraints();

    // center x
    handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);

    // center y
    handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
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
          + rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getWidth()));
      box.setHeight(
          rootBox.getHeight()
          - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getWidth())
          - rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getWidth()));
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

  private void handleWidthConstraint(
      final Box rootBox,
      final BoxConstraints rootBoxConstraints,
      final Box box,
      final BoxConstraints constraint) {
    int rootBoxX = rootBox.getX() + rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth());
    int rootBoxWidth = rootBox.getWidth() - rootBoxConstraints.getPaddingLeft().getValueAsInt(rootBox.getWidth()) - rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth());

    if (constraint.getHorizontalAlign() == HorizontalAlign.left) {
      box.setX(rootBoxX);
    } else if (constraint.getHorizontalAlign() == HorizontalAlign.right) {
      box.setX(rootBox.getWidth() - rootBoxConstraints.getPaddingRight().getValueAsInt(rootBox.getWidth()) - (int) constraint.getWidth().getValue(rootBoxWidth));
    } else {
      box.setX(rootBoxX + (rootBoxWidth - (int) constraint.getWidth().getValue(rootBoxWidth)) / 2);
    }

    box.setWidth((int) constraint.getWidth().getValue(rootBoxWidth));
  }

  private void handleHeightConstraint(
      final Box rootBox,
      final BoxConstraints rootBoxConstraints,
      final Box box,
      final BoxConstraints constraint) {
    int rootBoxY =
      rootBox.getY()
      + rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight());
    int rootBoxHeight =
      rootBox.getHeight()
      - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight())
      - rootBoxConstraints.getPaddingBottom().getValueAsInt(rootBox.getHeight());

    if (constraint.getVerticalAlign() == VerticalAlign.top) {
      box.setY(rootBoxY);
    } else if (constraint.getVerticalAlign() == VerticalAlign.bottom) {
      box.setY(
          rootBox.getHeight()
          - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight())
          - (int) constraint.getHeight().getValue(rootBoxHeight));
    } else {
      box.setY(
          rootBoxY
          + (rootBoxHeight - (int) constraint.getHeight().getValue(rootBoxHeight)) / 2);
    }

    box.setHeight((int) constraint.getHeight().getValue(rootBoxHeight));
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public SizeValue calculateConstraintWidth(final List < LayoutPart > children) {
    return null;
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public SizeValue calculateConstraintHeight(final List < LayoutPart > children) {
    return null;
  }
}
