package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * Created by void on 22.08.15.
 */
class NiftyTransformationNodeAccessor implements NiftyNodeAccessor<NiftyTransformationNode> {
  @Nonnull
  @Override
  public Class<NiftyTransformationNode> getNodeClass() {
    return NiftyTransformationNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<NiftyTransformationNode> getImplementation(@Nonnull final NiftyTransformationNode niftyNode) {
    return niftyNode.getImpl();
  }
}
