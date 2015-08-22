package de.lessvoid.nifty.node;

import de.lessvoid.nifty.node.NiftyRootNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

/**
 * Created by void on 08.08.15.
 */
class NiftyRootNodeImpl implements NiftyNodeImpl<NiftyRootNode> {

  @Override
  public NiftyRootNode getNiftyNode() {
    return new NiftyRootNode(this);
  }

  @Override
  public String toString() {
    return "NiftyRootNodeImpl{" + super.toString() + "}";
  }
}
