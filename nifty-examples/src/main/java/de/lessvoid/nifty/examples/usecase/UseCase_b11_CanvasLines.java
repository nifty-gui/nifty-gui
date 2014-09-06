package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.api.NiftyMutableColor;
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
      NiftyMutableColor mutableColor = new NiftyMutableColor(NiftyColor.WHITE());

      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        long time = nifty.getTimeProvider().getMsTime();

        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.beginPath();
        canvas.moveTo( 10.f, 100.f);
        canvas.lineTo(100.f, 90.f * (float) Math.sin(time / 900.) + 100.f);
        canvas.lineTo(200.f, 100.f);
        canvas.lineTo(300.f, 90.f * (float) Math.sin(time / 750.) + 100.f);
        canvas.lineTo(400.f, 100.f);
        canvas.lineTo(500.f, 90.f * (float) Math.sin(time / 1350.) + 100.f);

        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Round);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(mutableColor.linear(NiftyColor.WHITE(), NiftyColor.RED(), (Math.sin(time / 1500.) + 1.f) / 2.f).getColor());
        canvas.stroke();
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b11_CanvasLines.class, args);
  }
}
