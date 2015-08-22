package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

/**
 * Created by void on 22.08.15.
 */
class NiftyTransformationNodeAccessor implements NiftyNodeAccessor<NiftyTransformationNode> {
  @Override
  public Class<NiftyTransformationNode> getNodeClass() {
    return NiftyTransformationNode.class;
  }

  @Override
  public NiftyNodeImpl<NiftyTransformationNode> getImplementation(final NiftyTransformationNode niftyNode) {
    return niftyNode.getImpl();
  }
}
