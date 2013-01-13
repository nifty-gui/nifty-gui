package de.lessvoid.niftyimpl.layout.manager;

import java.util.List;

import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.Layoutable;

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
  public final void layoutElements(final Layoutable rootElement, final List < Layoutable > elements) {

    // make the params any sense?
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    // get the root box
    Box rootBox = rootElement.getLayoutPos();

    // now do the layout
    for (int i = 0; i < elements.size(); i++) {
      Layoutable p = elements.get(i);
      Box box = p.getLayoutPos();
      box.setX(rootBox.getX());
      box.setY(rootBox.getY());
      box.setWidth(rootBox.getWidth());
      box.setHeight(rootBox.getHeight());
    }
  }
}
