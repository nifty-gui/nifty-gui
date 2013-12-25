package de.lessvoid.nifty.layout.manager;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * OverlayLayout doesn't layout things. It just forwards the size of the
 * root box to the children.
 *
 * @author void
 */
public class OverlayLayout implements LayoutManager {

  /**
   * layoutElements.
   *
   * @param rootElement @see {@link LayoutManager}
   * @param elements    @see {@link LayoutManager}
   */
  @Override
  public final void layoutElements(
      @Nullable final LayoutPart rootElement,
      @Nullable final List<LayoutPart> elements) {

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
  @Nonnull
  @Override
  public final SizeValue calculateConstraintWidth(
      @Nonnull final LayoutPart root,
      @Nonnull final List<LayoutPart> children) {
    return SizeValue.def();
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  @Nonnull
  @Override
  public final SizeValue calculateConstraintHeight(
      @Nonnull final LayoutPart root,
      @Nonnull final List<LayoutPart> children) {
    return SizeValue.def();
  }
}
