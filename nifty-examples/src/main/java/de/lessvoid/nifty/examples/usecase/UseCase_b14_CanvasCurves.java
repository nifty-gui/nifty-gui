package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * An example how to draw bezier curves.
 * @author void
 */
public class UseCase_b14_CanvasCurves {

  public UseCase_b14_CanvasCurves(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    NiftyNode canvasNode = rootNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50));
    canvasNode.addCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setStrokeColor(NiftyColor.YELLOW());
        canvas.setLineWidth(5);
        canvas.beginPath();
        canvas.moveTo(50, 50);
        canvas.bezierCurveTo(50.f, 150.f, 250.f, 150.f, 250.f,  50.f);
        canvas.stroke();

        canvas.setStrokeColor(NiftyColor.BLACK());
        canvas.setLineWidth(5);
        canvas.beginPath();
        canvas.moveTo(350.f, 350.f);
        canvas.bezierCurveTo(350.f, 250.f, 450.f, 250.f, 450.f, 150.f);
        canvas.stroke();
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b14_CanvasCurves.class, args);
  }
}
