package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;

/**
 * A single root node that fills the whole screen.
 * @author void
 */
public class UseCase_0001_FullScreenColorPanel  {

  public UseCase_0001_FullScreenColorPanel(final Nifty nifty) {
    // Create a new root node that has the same size as the screen and fills it completely.
    NiftyNode niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Absolute);
  }
}
