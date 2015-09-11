package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class SimpleLayoutNodeAccessor implements NiftyNodeAccessor<SimpleLayoutNode> {
  @Nonnull
  @Override
  public Class<SimpleLayoutNode> getNodeClass() {
    return SimpleLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<SimpleLayoutNode> getImplementation(@Nonnull final SimpleLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
