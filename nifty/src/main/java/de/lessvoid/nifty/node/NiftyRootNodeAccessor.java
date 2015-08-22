package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

/**
 * Created by void on 22.08.15.
 */
class NiftyRootNodeAccessor implements NiftyNodeAccessor<NiftyRootNode> {
  @Override
  public Class<NiftyRootNode> getNodeClass() {
    return NiftyRootNode.class;
  }

  @Override
  public NiftyNodeImpl<NiftyRootNode> getImplementation(final NiftyRootNode niftyNode) {
    return niftyNode.getImpl();
  }
}
