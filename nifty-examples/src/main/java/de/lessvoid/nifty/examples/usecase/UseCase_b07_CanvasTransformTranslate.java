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
 * Demonstrate the translate transform of the NiftyCanvas.
 * @author void
 */
public class UseCase_b07_CanvasTransformTranslate {

  public UseCase_b07_CanvasTransformTranslate(final Nifty nifty) throws IOException {
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

        canvas.translate(60.f, 60.f);
        canvas.setFillStyle(NiftyColor.RED());
        canvas.fillRect(10, 10, 50, 50);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b07_CanvasTransformTranslate.class, args);
  }
}
