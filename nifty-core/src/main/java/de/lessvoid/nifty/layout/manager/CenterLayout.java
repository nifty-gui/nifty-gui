package de.lessvoid.nifty.layout.manager;

import java.util.List;
import java.util.logging.Logger;

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
  private Logger log = Logger.getLogger(CenterLayout.class.getName());

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

    if (elements.size() > 1) {
      log.warning("You're using a centerLayout element but you've added more than one child element to it. centerLayout only supports one element! Odd things will happen when used with more than one element :)");
    }

    // we only support center of the very first element
    Box rootBox = rootElement.getBox();
    BoxConstraints rootBoxConstraints = rootElement.getBoxConstraints();

    Box box = elements.get(0).getBox();
    BoxConstraints constraint = elements.get(0).getBoxConstraints();

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
      box.setY(rootBoxY + rootBox.getHeight() - rootBoxConstraints.getPaddingTop().getValueAsInt(rootBox.getHeight()) - boxHeight);
    } else {
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
