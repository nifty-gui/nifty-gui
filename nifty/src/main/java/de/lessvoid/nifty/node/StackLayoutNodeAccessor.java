package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class StackLayoutNodeAccessor implements NiftyNodeAccessor<StackLayoutNode> {
  @Nonnull
  @Override
  public Class<StackLayoutNode> getNodeClass() {
    return StackLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<StackLayoutNode> getImplementation(@Nonnull final StackLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
