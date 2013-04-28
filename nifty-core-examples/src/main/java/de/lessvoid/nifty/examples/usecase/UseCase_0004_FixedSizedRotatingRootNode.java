package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_0004_FixedSizedRotatingRootNode implements UseCase {
  private final NiftyNode niftyNode;
  private float rot = 0;

  public UseCase_0004_FixedSizedRotatingRootNode(final Nifty nifty) {
    // Create a new root node that is exactly 100x100px and is centered.
    niftyNode = nifty.createRootNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.None);
    niftyNode.setBackgroundColor(NiftyColor.BLUE());
  }

  @Override
  public void update(final Nifty nifty) {
    niftyNode.setRotation(rot);
    rot += 0.01;
  }
}
