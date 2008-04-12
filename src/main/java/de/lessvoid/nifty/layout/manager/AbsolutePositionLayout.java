package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * AbsolutPositionLayout doesn't layout things. It just
 * absolute position it according to the constraints.
 *
 * @author void
 */
public class AbsolutePositionLayout implements LayoutManager {

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
      BoxConstraints cons = p.getBoxConstraints();

      // makes only sense with constraints given
      if (cons != null) {
        if (cons.getX() != null) {
          box.setX(
              rootBox.getX() + cons.getX().getValueAsInt(rootBox.getWidth()));
        }

        if (cons.getY() != null) {
          box.setY(
              rootBox.getY() + cons.getY().getValueAsInt(rootBox.getHeight()));
        }

        if (cons.getWidth() != null) {
          box.setWidth(cons.getWidth().getValueAsInt(rootBox.getWidth()));
        }

        if (cons.getHeight() != null) {
          box.setHeight(cons.getHeight().getValueAsInt(rootBox.getHeight()));
        }
      }
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
