package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class DockLayoutChildNodeAccessor implements NiftyNodeAccessor<DockLayoutChildNode> {
  @Nonnull
  @Override
  public Class<DockLayoutChildNode> getNodeClass() {
    return DockLayoutChildNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<DockLayoutChildNode> getImplementation(@Nonnull final DockLayoutChildNode niftyNode) {
    return niftyNode.getImpl();
  }
}
