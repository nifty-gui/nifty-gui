package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class SizeLayoutNodeAccessor implements NiftyNodeAccessor<SizeLayoutNode> {
  @Nonnull
  @Override
  public Class<SizeLayoutNode> getNodeClass() {
    return SizeLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<SizeLayoutNode> getImplementation(@Nonnull final SizeLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
