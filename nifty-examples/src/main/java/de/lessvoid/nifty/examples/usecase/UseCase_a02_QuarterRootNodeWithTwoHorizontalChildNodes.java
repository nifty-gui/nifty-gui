package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node that is only a quarter of the screen, placed in the upper right corner and contains two child
 * nodes using a horizontal child layout.
 *
 * @author void
 */
public class UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes {
  NiftyNode rootNode;

  public UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes(final Nifty nifty) {
    // Change the root node placement child layout from the default Center to Vertical. This will be used to lay out
    // the root node on the screen in the upper half of the screen. We'll make the root node 50% of the screen.
    nifty.setRootNodePlacementLayout(ChildLayout.Vertical);
    rootNode = nifty.createRootNode(UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Horizontal);

    // By changing the rootNode horizontal alignment we move it to the right. This makes it appear in the upper right.
    rootNode.setHAlign(HAlign.right);

    // add two child nodes to the root node
    rootNode.newChildNode().setBackgroundColor(NiftyColor.fromString("#ff08"));
    rootNode.newChildNode().setBackgroundColor(NiftyColor.fromString("#ff0f"));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes.class, args);
  }
}
