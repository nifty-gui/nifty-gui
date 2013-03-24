package de.lessvoid.nifty.internal.layout;

import java.util.List;

import de.lessvoid.nifty.internal.Box;

/**
 * OverlayLayout doesn't layout things. It just forwards the size of the
 * root box to the children.
 *
 * @author void
 */
/* package */ class LayoutOverlay implements Layout {

  public void layoutElements(final Layoutable root, final List <Layoutable> children) {
    if (root == null || children == null || children.size() == 0) {
      return;
    }

    Box rootBox = root.getLayoutPos();
    for (int i = 0; i < children.size(); i++) {
      Layoutable p = children.get(i);
      Box box = p.getLayoutPos();
      box.setX(rootBox.getX());
      box.setY(rootBox.getY());
      box.setWidth(rootBox.getWidth());
      box.setHeight(rootBox.getHeight());
    }
  }
}
