package de.lessvoid.nifty.internal.render;

import java.util.Comparator;

import javax.annotation.Nonnull;

/**
 * This uses the renderOrder attribute of the elements to compare them. If the renderOrder
 * attribute is not set (is 0) then the index of the element in the elements list is used
 * as the renderOrder value. This is done to keep the original sort order of the elements for
 * rendering. The value is not cached and is directly recalculated using the element index in
 * the list. The child count for an element is usually low (< 10) and the comparator is only
 * executed when the child elements change. So this lookup shouldn't hurt performance too much.
 * <p/>
 * If you change the default value of renderOrder then your value is being used. So if you set it
 * to some high value (> 1000 to be save) this element is rendered after all the other elements.
 * If you set it to some very low value (< -1000 to be save) then this element is rendered before
 * all the others.
 */
final class RenderNodeComparator implements Comparator<RenderNode> {

  @Override
  public int compare(@Nonnull final RenderNode o1, @Nonnull final RenderNode o2) {
    if (o1 == o2) {
      return 0;
    }
    int o1RenderOrder = getRenderOrder(o1);
    int o2RenderOrder = getRenderOrder(o2);

    if (o1RenderOrder < o2RenderOrder) {
      return -1;
    } else if (o1RenderOrder > o2RenderOrder) {
      return 1;
    }

    // this means the renderOrder values are equal. since this is a set
    // we can't return 0 because this would mean (for the set) that the
    // elements are equal and one of the values will be removed. so here
    // we simply compare the String representation of the elements so that
    // we keep a fixed sort order.
    Integer o1Id = o1.getNodeId();
    Integer o2Id = o2.getNodeId();
    return o1Id.compareTo(o2Id);
  }

  private int getRenderOrder(@Nonnull final RenderNode renderNode) {
    if (renderNode.getRenderOrder() != 0) {
      return renderNode.getRenderOrder();
    }
    return renderNode.getIndexInParent();
  }
}
