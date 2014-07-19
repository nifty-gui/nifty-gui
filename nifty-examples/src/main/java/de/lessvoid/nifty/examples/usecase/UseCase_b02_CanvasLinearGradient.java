package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * custom canvas painter rendering gradients.
 * @author void
 */
public class UseCase_b02_CanvasLinearGradient {
  private final NiftyNode niftyNode;
  private final NiftyNode child;

  public UseCase_b02_CanvasLinearGradient(final Nifty nifty) {
    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.TRANSPARENT());

    child = niftyNode.newChildNode(UnitValue.percent(100), UnitValue.percent(100));
    child.setBackgroundColor(NiftyColor.RED());
    child.setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 0.0, 1.0, 0.0);
        gradient.addColorStop(0.0, NiftyColor.RED());
        gradient.addColorStop(1.0, NiftyColor.WHITE());
        canvas.setFillStyle(gradient);
        canvas.fillRect(50, 50, 350, 100);

        gradient = new NiftyLinearGradient(0.0, 0.0, 0.0, 1.0);
        gradient.addColorStop(0.0, NiftyColor.GREEN());
        gradient.addColorStop(1.0, NiftyColor.BLACK());
        canvas.setFillStyle(gradient);
        canvas.fillRect(50, 150, 350, 200);

        gradient = new NiftyLinearGradient(0.0, 0.0, 0.5, 1.0);
        gradient.addColorStop(0.0, NiftyColor.WHITE());
        gradient.addColorStop(1.0, NiftyColor.BLACK());
        canvas.setFillStyle(gradient);
        canvas.fillRect(50, 250, 350, 350);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b02_CanvasLinearGradient.class, args);
  }
}
