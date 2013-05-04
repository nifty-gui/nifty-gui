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
  private final NiftyNode childNode;
  private float rot = 0;

  public UseCase_0004_FixedSizedRotatingRootNode(final Nifty nifty) {
    niftyNode = nifty.createRootNode(UnitValue.px(300), UnitValue.px(300), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.BLUE());

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100));
    childNode.setBackgroundColor(NiftyColor.RED());
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    rot += deltaTime / 50.f;
    //niftyNode.setRotation(rot/10);
    childNode.setRotation(-rot*10);
  }
}
