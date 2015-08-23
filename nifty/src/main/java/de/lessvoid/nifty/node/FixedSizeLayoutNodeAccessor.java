package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class FixedSizeLayoutNodeAccessor implements NiftyNodeAccessor<FixedSizeLayoutNode> {
  @Nonnull
  @Override
  public Class<FixedSizeLayoutNode> getNodeClass() {
    return FixedSizeLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<FixedSizeLayoutNode> getImplementation(@Nonnull final FixedSizeLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
