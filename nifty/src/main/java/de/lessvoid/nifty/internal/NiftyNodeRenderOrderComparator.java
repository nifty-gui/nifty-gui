package de.lessvoid.nifty.internal;

import java.util.Comparator;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;

/**
 * This uses the renderOrder attribute of the NiftyNodes to compare them. If the renderOrder
 * attribute is not set (is 0) then the index of the element in the elements list is used
 * as the renderOrder value. This is done to keep the original sort order of the elements for
 * input processing.
 */
public class NiftyNodeRenderOrderComparator implements Comparator<NiftyNode> {

  @Override
  public int compare(@Nonnull final NiftyNode o1, @Nonnull final NiftyNode o2) {
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
    String o1Id = o1.toString();
    String o2Id = o2.toString();
    return o1Id.compareTo(o2Id);
  }

  private int getRenderOrder(@Nonnull final NiftyNode niftyNode) {
    if (niftyNode.getRenderOrder() != 0) {
      return niftyNode.getRenderOrder();
    }
    return NiftyNodeAccessor.getDefault().getInternalNiftyNode(niftyNode).getInputOrderIndex();
  }
}
