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
 * An example how to draw arcs into a NiftyCanvas.
 * @author void
 */
public class UseCase_b12_CanvasArc {
  public UseCase_b12_CanvasArc(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    NiftyNode childNode = rootNode.newChildNode(UnitValue.px(512), UnitValue.px(512));
    childNode.startAnimatedRedraw(0, 10);
    childNode.addCanvasPainter(new NiftyCanvasPainter() {
      float v = 0;
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.beginPath();
        canvas.arc(256., 256., 100., v * Math.PI, 2 * Math.PI);
        v += 0.005;
        if (v > 2) {
          v -= 2;
        }
        canvas.setLineWidth(v * 100);
        canvas.setStrokeColor(NiftyColor.WHITE());
        canvas.stroke();

        canvas.beginPath();
        canvas.arc(256., 256., 100., 0, 2 * Math.PI);
        canvas.setLineWidth(1);
        canvas.setStrokeColor(NiftyColor.BLACK());
        canvas.stroke();
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b12_CanvasArc.class, args);
  }
}
