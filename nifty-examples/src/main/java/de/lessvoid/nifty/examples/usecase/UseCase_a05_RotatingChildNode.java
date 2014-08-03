package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_a05_RotatingChildNode {

  public UseCase_a05_RotatingChildNode(final Nifty nifty) {
    nifty.clearScreenBeforeRender();

    final NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());
    niftyNode.setPivot(0.5, 0.5);

    final NiftyNode childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.BLACK());
    childNode.setPivot(0.5, 0.5);

    final NiftyNode grandChildNode = childNode.newChildNode(UnitValue.px(25), UnitValue.px(25));
    grandChildNode.setBackgroundColor(NiftyColor.RED());
    grandChildNode.setPivot(0.5, 0.5);

    niftyNode.startAnimated(0, 25, new NiftyCallback<Float>() {
      private float rot = 0;

      @Override
      public void execute(final Float totalTime) {
        rot += 1.f;

        childNode.setRotationX(rot);
        childNode.setRotationY(rot);
        childNode.setRotationZ(rot);

        grandChildNode.setRotationZ(rot*10);

        niftyNode.setScaleX((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
        niftyNode.setScaleY((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a05_RotatingChildNode.class, args);
  }
}
