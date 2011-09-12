package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * OverlayLayout doesn't layout things. It just forwards the size of the
 * root box to the children.
 *
 * @author void
 */
public class OverlayLayout implements LayoutManager {

  /**
   * layoutElements.
   * @param rootElement @see {@link LayoutManager}
   * @param elements @see {@link LayoutManager}
   */
  public final void layoutElements(
      final LayoutPart rootElement,
      final List < LayoutPart > elements) {

    // make the params any sense?
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    // get the root box
    Box rootBox = rootElement.getBox();

    // now do the layout
    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      Box box = p.getBox();
      box.setX(rootBox.getX());
      box.setY(rootBox.getY());
      box.setWidth(rootBox.getWidth());
      box.setHeight(rootBox.getHeight());
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
    return null;
  }
}
