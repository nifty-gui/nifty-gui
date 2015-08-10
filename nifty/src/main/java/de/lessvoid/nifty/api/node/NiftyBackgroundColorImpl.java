package de.lessvoid.nifty.api.node;

import de.lessvoid.nifty.spi.NiftyNodeImpl;

/**
 * Created by void on 09.08.15.
 */
class NiftyBackgroundColorImpl implements NiftyNodeImpl<NiftyBackgroundColor> {
  private NiftyBackgroundColor niftyNode;

  @Override
  public void initialize(final NiftyBackgroundColor niftyNode) {
    this.niftyNode = niftyNode;
  }

  @Override
  public NiftyBackgroundColor getNiftyNode() {
    return niftyNode;
  }
}
