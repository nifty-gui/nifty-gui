package de.lessvoid.nifty.internal.layout;

import java.util.List;


/**
 * The LayoutManager defines the basic interface to layout a list of child elements.
 *
 * Implementation note:
 *
 * A LayoutManager implementation will be instantiated only once and the same instance will be shared by multiple
 * Nodes. This is done to save runtime and memory costs when using lots of Nodes. Therefore it's probably a good
 * idea to not use any instance variables in LayoutManager implementations.
 *
 * @author void
 */
public interface InternalLayout {

  /**
   * Layout the given list of NiftyNodes using the given root NiftyNode as the parent.
   *
   * @param root root NiftyNode
   * @param children child nodes of the root element
   */
  void layoutElements(InternalLayoutable root, List <? extends InternalLayoutable> children);
}
