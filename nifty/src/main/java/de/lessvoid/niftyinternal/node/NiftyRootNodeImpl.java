package de.lessvoid.niftyinternal.node;

import de.lessvoid.nifty.node.NiftyRootNode;
import de.lessvoid.nifty.spi.NiftyNodeImpl;

/**
 * Created by void on 08.08.15.
 */
public class NiftyRootNodeImpl implements NiftyNodeImpl<NiftyRootNode> {
  private NiftyRootNode niftyRootNode;

  @Override
  public void initialize(final NiftyRootNode niftyRootNode) {
    this.niftyRootNode = niftyRootNode;
  }

  @Override
  public NiftyRootNode getNiftyNode() {
    return niftyRootNode;
  }
}
