package de.lessvoid.nifty;

import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class NiftyNodeStringAccessor implements NiftyNodeAccessor<NiftyNodeString> {
  @Nonnull
  @Override
  public Class<NiftyNodeString> getNodeClass() {
    return NiftyNodeString.class;
  }

  @Nonnull
  @Override
  public NiftyNodeImpl<NiftyNodeString> getImplementation(@Nonnull NiftyNodeString niftyNode) {
    return niftyNode.getImpl();
  }
}
