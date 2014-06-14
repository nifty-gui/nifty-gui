package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyImage;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * custom canvas painter for rendering images
 * @author void
 */
public class UseCase_b04_CanvasImage {
  private final NiftyNode niftyNode;

  public UseCase_b04_CanvasImage(final Nifty nifty) {
    final NiftyImage image = nifty.createNiftyImage("nifty.png");

    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setContent(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());
        canvas.image(image, 10, 10);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b04_CanvasImage.class, args);
  }
}
