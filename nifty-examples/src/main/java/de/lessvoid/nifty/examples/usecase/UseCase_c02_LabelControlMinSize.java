package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.controls.Label;

/**
 * Show case that a Label without any size constraints will be scaled to the minimum text size.
 * @author void
 */
public class UseCase_c02_LabelControlMinSize {
  public UseCase_c02_LabelControlMinSize(final Nifty nifty) throws IOException {
    NiftyFont font = nifty.createFont("fonts/aurulent-sans-16.fnt");
    NiftyNode niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Center);

    Label label = niftyNode.newControl(Label.class);
    label.setFont(font);
    label.setText("hello autosize label");
    label.setColor(NiftyColor.fromString("#ffff"));
    label.getNode().setBackgroundColor(NiftyColor.RED());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c02_LabelControlMinSize.class, args);
  }
}
