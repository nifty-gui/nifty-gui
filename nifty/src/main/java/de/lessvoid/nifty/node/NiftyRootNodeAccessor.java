package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * Created by void on 22.08.15.
 */
class NiftyRootNodeAccessor implements NiftyNodeAccessor<NiftyRootNode> {
  @Nonnull
  @Override
  public Class<NiftyRootNode> getNodeClass() {
    return NiftyRootNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<NiftyRootNode> getImplementation(@Nonnull final NiftyRootNode niftyNode) {
    return niftyNode.getImpl();
  }
}
