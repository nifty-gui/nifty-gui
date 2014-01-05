package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node with a fixed size and a blue background color.
 * @author void
 */
public class UseCase_0003_FixedSizedRootNode {

  public UseCase_0003_FixedSizedRootNode(final Nifty nifty) {
    // Create a new root node that is exactly 100x100px and is centered.
    NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.None);
    niftyNode.setBackgroundColor(NiftyColor.BLUE());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_0003_FixedSizedRootNode.class, args);
  }
}
