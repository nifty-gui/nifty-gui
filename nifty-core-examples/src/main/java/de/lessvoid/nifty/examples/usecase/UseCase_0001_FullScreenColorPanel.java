package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;

/**
 * Render a single root node that fills the whole screen.
 * @author void
 */
public class UseCase_0001_FullScreenColorPanel  {
  private final Nifty nifty;

  public UseCase_0001_FullScreenColorPanel(final Nifty niftyParam) {
    nifty = niftyParam;

    // Create a new root node that is the same size as the screen and fills the screen completely.
    NiftyNode niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Absolute);

    // Output the scene we have so far to the console
    StringBuilder result = new StringBuilder();
    niftyNode.getStateInfo(result);
    System.out.println(result);
  }
}
