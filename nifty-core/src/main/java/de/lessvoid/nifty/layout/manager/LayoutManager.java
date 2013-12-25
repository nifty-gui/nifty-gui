package de.lessvoid.nifty.layout.manager;

import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * The Layout interface must be implemented by all Layout Managers.
 * It defines the basic interface to layout components.
 * <p/>
 * Implementation note:
 * <p/>
 * Most of the standard LayoutManager implementation will only be instantiated once and the same instance will be
 * shared by multiple Elements. This is done to save runtime and memory costs when using lots of Elements.
 * That's why it's probably a good idea to not use any instance variables in LayoutManager implementations!
 *
 * @author void
 */
public interface LayoutManager {

  /**
   * Layout the given elements using the given root element as the
   * parent element. The actual layout algorithm is performed by
   * the Layout implementations.
   *
   * @param root     root element all children belong to
   * @param children children elements of the root element
   */
  void layoutElements(@Nonnull LayoutPart root, @Nonnull List<LayoutPart> children);

  /**
   * Calculates a new Width constraint. Note that it is the callers
   * responsibility to find out if this is possible. At the moment
   * this is only possible when all child elements have a width constraint
   * set in px.
   *
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  @Nonnull
  SizeValue calculateConstraintWidth(@Nonnull LayoutPart root, @Nonnull List<LayoutPart> children);

  /**
   * Calculates a new Height constraint. Note that it is the callers
   * responsibility to find out if this is possible. At the moment
   * this is only possible when all child elements have a Height constraint
   * set in px.
   *
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  @Nonnull
  SizeValue calculateConstraintHeight(@Nonnull LayoutPart root, @Nonnull List<LayoutPart> children);
}
