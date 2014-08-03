package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * Demonstrate the rotate transform of the NiftyCanvas.
 * @author void
 */
public class UseCase_b08_CanvasTransformRotate {

  public UseCase_b08_CanvasTransformRotate(final Nifty nifty) throws IOException {
    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);

    NiftyNode child = rootNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50));
    child.setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        // fill the whole node content with a plain white color
        canvas.setFillStyle(NiftyColor.WHITE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setFillStyle(NiftyColor.BLACK());
        canvas.fillRect(10, 10, 50, 50);

        canvas.setFillStyle(NiftyColor.RED());
        canvas.rotateDegrees(20.f);
        canvas.fillRect(10, 10, 50, 50);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b08_CanvasTransformRotate.class, args);
  }
}
