package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * Display a simple text.
 * @author void
 */
public class UseCase_0006_SimpleText {
  private final NiftyNode niftyNode;

  public UseCase_0006_SimpleText(final Nifty nifty) {
    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());
/*
    Text text = niftyNode.newControl(Text.class);
    text.setText("Hello Nifty Text");
    text.setColor(NiftyColor.RED());
*/
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_0006_SimpleText.class, args);
  }
}
