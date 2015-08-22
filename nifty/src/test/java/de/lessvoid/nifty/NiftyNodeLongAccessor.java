package de.lessvoid.nifty;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class NiftyNodeLongAccessor implements NiftyNodeAccessor<NiftyNodeLong> {
  @Nonnull
  @Override
  public Class<NiftyNodeLong> getNodeClass() {
    return NiftyNodeLong.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<NiftyNodeLong> getImplementation(@Nonnull NiftyNodeLong niftyNode) {
    return niftyNode.getImpl();
  }
}
