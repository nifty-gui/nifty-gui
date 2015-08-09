package de.lessvoid.nifty.api.node;

import de.lessvoid.nifty.spi.NiftyNodeImpl;

/**
 * Created by void on 08.08.15.
 */
public class RootNodeImpl implements NiftyNodeImpl {
  private final RootNode rootNode;

  public RootNodeImpl(final RootNode rootNode) {
    this.rootNode = rootNode;
  }
}
