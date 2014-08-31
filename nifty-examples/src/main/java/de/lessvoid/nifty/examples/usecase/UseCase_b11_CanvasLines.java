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
 * An example how to draw lines into a NiftyCanvas.
 * @author void
 */
public class UseCase_b11_CanvasLines {

  public UseCase_b11_CanvasLines(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    NiftyNode childNode = rootNode.newChildNode(UnitValue.px(512), UnitValue.px(512));
    childNode.startAnimatedRedraw(0, 16);
    childNode.addCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.fromString("#00ff"));
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        long time = nifty.getTimeProvider().getMsTime();
        canvas.beginPath();
        canvas.moveTo( 10.f, 100.f);
        canvas.lineTo(100.f, 90.f * (float) Math.sin(time / 1000.) + 100.f);
        canvas.lineTo(200.f, 100.f);
        canvas.lineTo(300.f, 90.f * (float) Math.sin(time / 1250.) + 100.f);
        canvas.lineTo(400.f, 100.f);
        canvas.lineTo(500.f, 90.f * (float) Math.sin(time / 1500.) + 100.f);
        canvas.stroke();
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b11_CanvasLines.class, args);
  }
}
