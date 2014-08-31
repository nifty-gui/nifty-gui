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

  public UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes(final Nifty nifty) {
    // By changing the rootNode horizontal alignment we move it to the right. This makes it appear in the upper right.
    NiftyNode rootNode = nifty.createRootNode(ChildLayout.Vertical, UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Horizontal);
    rootNode.setHAlign(HAlign.right);

    // add two child nodes to the root node
    rootNode.newChildNode().setBackgroundColor(NiftyColor.fromString("#ff08"));
    rootNode.newChildNode().setBackgroundColor(NiftyColor.fromString("#ff0f"));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes.class, args);
  }
}
