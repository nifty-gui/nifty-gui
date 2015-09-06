package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class AbsoluteLayoutNodeAccessor implements NiftyNodeAccessor<AbsoluteLayoutNode> {
  @Nonnull
  @Override
  public Class<AbsoluteLayoutNode> getNodeClass() {
    return AbsoluteLayoutNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<AbsoluteLayoutNode> getImplementation(@Nonnull final AbsoluteLayoutNode niftyNode) {
    return niftyNode.getImpl();
  }
}
