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
public class UseCase_a04_RotatingRootNode {
  private final NiftyNode niftyNode;
  private final NiftyNode childNode;

  public UseCase_a04_RotatingRootNode(final Nifty nifty) {
    nifty.clearScreenBeforeRender();

    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Absolute);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());
    niftyNode.startAnimated(0, 15, new NiftyCallback<Float>() {
      private float angle = 0;

      @Override
      public void execute(final Float time) {
        niftyNode.setRotationZ(angle++);
      }
    });

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.RED());
    childNode.setXConstraint(UnitValue.px(50));
    childNode.setYConstraint(UnitValue.px(50));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a04_RotatingRootNode.class, args);
  }
}
