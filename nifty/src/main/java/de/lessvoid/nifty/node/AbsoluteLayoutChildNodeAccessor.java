package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class AbsoluteLayoutChildNodeAccessor implements NiftyNodeAccessor<AbsoluteLayoutChildNode> {
  @Nonnull
  @Override
  public Class<AbsoluteLayoutChildNode> getNodeClass() {
    return AbsoluteLayoutChildNode.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<AbsoluteLayoutChildNode> getImplementation(@Nonnull final AbsoluteLayoutChildNode niftyNode) {
    return niftyNode.getImpl();
  }
}
