package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.controls.Label;

/**
 * Display a simple text.
 * @author void
 */
public class UseCase_c01_LabelControl {
  private final NiftyNode niftyNode;

  public UseCase_c01_LabelControl(final Nifty nifty) throws IOException {
    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());

    Label label = niftyNode.newControl(Label.class);
    label.setFont(nifty.createFont("fonts/verdana-small-regular.fnt"));
    label.setText("Hello Nifty 2.0");
    label.getNode().setBackgroundColor(NiftyColor.BLUE());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c01_LabelControl.class, args);
  }
}
