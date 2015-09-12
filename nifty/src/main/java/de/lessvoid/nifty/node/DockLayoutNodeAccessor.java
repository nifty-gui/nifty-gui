package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class DockLayoutNodeAccessor implements NiftyNodeAccessor<DockLayoutNode> {
  @Nonnull
  @Override
  public Class<DockLayoutNode> getNodeClass() {
    return DockLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<DockLayoutNode> getImplementation(@Nonnull final DockLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
