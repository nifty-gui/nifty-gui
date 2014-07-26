package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * custom canvas painter using a custom fragment shader ... read that last sentence again! =D
 * @author void
 */
public class UseCase_b05_CanvasCustomShader implements UseCaseUpdateable {
  private final NiftyNode rootNode;
  private final NiftyNode childNode;
  private float angle;

  public UseCase_b05_CanvasCustomShader(final Nifty nifty) throws IOException {
    rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setCanvasPainter(nifty.customShaderCanvasPainter("shaders/custom.fs"));

    childNode = rootNode.newChildNode(UnitValue.percent(25), UnitValue.percent(25));
    childNode.setCanvasPainter(nifty.customShaderCanvasPainter("shaders/custom.fs"));
    childNode.setBlendMode(BlendMode.MULTIPLY);
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b05_CanvasCustomShader.class, args);
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    rootNode.requestRedraw();
    childNode.requestRedraw();
    childNode.setRotationZ(angle/10.0);
    childNode.setScaleX(Math.sin(angle/500.0) + 1.5);
    childNode.setScaleY(Math.sin(angle/500.0) + 1.5);
    angle += deltaTime;
  }
}
