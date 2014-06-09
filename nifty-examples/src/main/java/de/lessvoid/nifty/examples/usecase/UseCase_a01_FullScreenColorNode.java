package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;

/**
 * A single root node that fills the whole screen and has a red background color.
 * @author void
 */
public class UseCase_a01_FullScreenColorNode {
  private NiftyNode niftyNode;

  public UseCase_a01_FullScreenColorNode(final Nifty nifty) {
    // Create a new root node that has the same size as the screen.
    niftyNode = nifty.createRootNodeFullscreen();
    niftyNode.setBackgroundColor(NiftyColor.RED());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a01_FullScreenColorNode.class, args);
  }
}
