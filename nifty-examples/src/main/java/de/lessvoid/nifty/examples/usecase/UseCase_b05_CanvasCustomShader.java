package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;

/**
 * custom canvas painter using a custom fragment shader ... read that last sentence again! =D
 * @author void
 */
public class UseCase_b05_CanvasCustomShader {

  public UseCase_b05_CanvasCustomShader(final Nifty nifty) throws IOException {
    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setCanvasPainter(nifty.customShaderCanvasPainter("shaders/custom.fs"));
    rootNode.startAnimatedRedraw(0, 10);
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b05_CanvasCustomShader.class, args);
  }
}
