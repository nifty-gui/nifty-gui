package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class UniformStackLayoutNodeAccessor implements NiftyNodeAccessor<UniformStackLayoutNode> {
  @Nonnull
  @Override
  public Class<UniformStackLayoutNode> getNodeClass() {
    return UniformStackLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<UniformStackLayoutNode> getImplementation(@Nonnull final UniformStackLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
