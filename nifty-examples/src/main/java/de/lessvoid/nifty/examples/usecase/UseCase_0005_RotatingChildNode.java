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
public class UseCase_0005_RotatingChildNode implements UseCaseUpdateable {
  private final NiftyNode niftyNode;
  private final NiftyNode childNode;
  private final NiftyNode grandChildNode;
  private float rot = 0;
  private float totalTime = 0;

  public UseCase_0005_RotatingChildNode(final Nifty nifty) {
    nifty.clearScreenBeforeRender();

    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());
    niftyNode.setPivot(0.5, 0.5);

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.BLACK());
    childNode.setPivot(0.5, 0.5);

    grandChildNode = childNode.newChildNode(UnitValue.px(25), UnitValue.px(25));
    grandChildNode.setBackgroundColor(NiftyColor.RED());
    grandChildNode.setPivot(0.5, 0.5);
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    totalTime += deltaTime;

    if (totalTime > 25) {
      totalTime = 0;
      rot += 1.f;

      childNode.setRotationX(rot);
      childNode.setRotationY(rot);
      childNode.setRotationZ(rot);

      grandChildNode.setRotationZ(rot*10);

      niftyNode.setScaleX((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
      niftyNode.setScaleY((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
    }
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_0005_RotatingChildNode.class, args);
  }
}
