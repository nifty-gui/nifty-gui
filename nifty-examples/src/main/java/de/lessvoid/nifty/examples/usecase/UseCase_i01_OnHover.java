package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * Mouse hover over an element.
 * @author void
 */
public class UseCase_i01_OnHover {

  public UseCase_i01_OnHover(final Nifty nifty) {
    NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(256), UnitValue.px(256), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.BLUE());

    final NiftyNode childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100));
    childNode.setBackgroundColor(NiftyColor.RED());
    childNode.startAnimated(0, 16, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float t) {
        childNode.setRotationZ(t * 100.f);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_i01_OnHover.class, args);
  }
}
