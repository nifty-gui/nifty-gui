package de.lessvoid.niftyimpl.layout.manager;

import java.util.List;

import de.lessvoid.niftyimpl.layout.Layoutable;


/**
 * The LayoutManager interface must be implemented by all Layout algorithms. It defines the basic interface to layout
 * child elements.
 *
 * Implementation note:
 *
 * A LayoutManager implementation will be instantiated only once and the same instance will be shared by multiple
 * Elements. This is done to save runtime and memory costs when using lots of Elements. That's why it's probably a good
 * idea to not use any instance variables in LayoutManager implementations.
 *
 * @author void
 */
public interface LayoutManager {

  /**
   * Layout the given list of Layoutables using the given root Layoutable as the parent. The actual layout algorithm is
   * performed by the actual LayoutManager implementation.
   *
   * @param root root Layoutable
   * @param children child elements of the root element
   */
  void layoutElements(Layoutable root, List < Layoutable > children);
}
