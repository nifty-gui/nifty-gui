package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
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
  public final void layoutElements(
      final LayoutPart rootElement,
      final List < LayoutPart > elements) {

    // check for useful params
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    // we only support center of the very first element
    Box rootBox = rootElement.getBox();

    Box box = elements.get(0).getBox();
    BoxConstraints constraint = elements.get(0).getBoxConstraints();

    // center x
    if (constraint.getWidth() != null) {
      if (constraint.getHorizontalAlign() == HorizontalAlign.left) {
        box.setX(rootBox.getX());
      } else if (constraint.getHorizontalAlign() == HorizontalAlign.right) {
        box.setX(
            rootBox.getX()
            +
            rootBox.getWidth()
            -
            (int) constraint.getWidth().getValue(rootBox.getWidth()));
      } else {
        box.setX(
            rootBox.getX()
            +
            (rootBox.getWidth() - (int) constraint.getWidth().getValue(rootBox.getWidth())) / 2);
      }
      box.setWidth((int) constraint.getWidth().getValue(rootBox.getWidth()));
    } else {
      box.setX(rootBox.getX());
      box.setWidth(rootBox.getWidth());
    }

    // center y
    if (constraint.getHeight() != null) {
      box.setY(rootBox.getY() + (rootBox.getHeight() - (int) constraint.getHeight().getValue(rootBox.getHeight())) / 2);
      box.setHeight((int) constraint.getHeight().getValue(rootBox.getHeight()));
    } else {
      box.setY(rootBox.getY());
      box.setHeight(rootBox.getHeight());
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
    return null;
  }
}
