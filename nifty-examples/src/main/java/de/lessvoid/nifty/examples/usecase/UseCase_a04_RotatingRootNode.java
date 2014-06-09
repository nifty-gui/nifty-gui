package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_a04_RotatingRootNode implements UseCaseUpdateable {
  private final NiftyNode niftyNode;
  private final NiftyNode childNode;
  private float totalTime = 0;
  private float angle = 0;

  public UseCase_a04_RotatingRootNode(final Nifty nifty) {
    nifty.clearScreenBeforeRender();

    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Absolute);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.RED());
    childNode.setXConstraint(UnitValue.px(50));
    childNode.setYConstraint(UnitValue.px(50));
}

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    totalTime += deltaTime;
    if (totalTime > 15) {
      niftyNode.setRotationZ(angle++);
      totalTime = 0;
    }
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a04_RotatingRootNode.class, args);
  }
}
