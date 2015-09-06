package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class PaddingLayoutNodeAccessor implements NiftyNodeAccessor<PaddingLayoutNode> {
  @Nonnull
  @Override
  public Class<PaddingLayoutNode> getNodeClass() {
    return PaddingLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<PaddingLayoutNode> getImplementation(@Nonnull final PaddingLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
