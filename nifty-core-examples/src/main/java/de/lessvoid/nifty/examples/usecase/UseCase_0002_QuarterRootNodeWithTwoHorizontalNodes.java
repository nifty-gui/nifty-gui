package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;

/**
 * Render a single root node that is only a quarter of the screen and contains two child nodes using a
 * horizontal child layout.
 *
 * @author void
 */
public class UseCase_0002_QuarterRootNodeWithTwoHorizontalNodes  {
  private final Nifty nifty;

  public UseCase_0002_QuarterRootNodeWithTwoHorizontalNodes(final Nifty nifty) {
    this.nifty = nifty;

    // Change the root node placement child layout to some other value
    nifty.setRootNodePlacementLayout(ChildLayout.Vertical);

    // Create a new root node that is the same size as the screen and fills the screen completely.
    NiftyNode rootNode = nifty.createRootNode(UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Horizontal);
    rootNode.setHorizontalAlignment(HorizontalAlignment.right);

    rootNode.createChildNode();
    rootNode.createChildNode();

    StringBuilder result = new StringBuilder();
    rootNode.getStateInfo(result);
    System.out.println(result);
  }
}
