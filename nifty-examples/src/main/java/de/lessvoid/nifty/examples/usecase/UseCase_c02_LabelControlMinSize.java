package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.controls.Label;

/**
 * custom canvas painter using a custom fragment shader ... read that last sentence again! =D
 * @author void
 */
public class UseCase_c02_LabelControlMinSize implements UseCaseUpdateable {
  private final NiftyNode niftyNode;
  private final Label label;

  public UseCase_c02_LabelControlMinSize(final Nifty nifty) throws IOException {
    NiftyFont font = nifty.createFont("fonts/aurulent-sans-16.fnt");

    niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    niftyNode.setCanvasPainter(nifty.customShaderCanvasPainter("shaders/custom.fs"));

    label = niftyNode.newControl(Label.class);
    label.setFont(font);
    label.setText("Hello custom shader");
    label.setColor(NiftyColor.fromString("#ffff"));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c02_LabelControlMinSize.class, args);
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    niftyNode.requestRedraw();
  }
}
