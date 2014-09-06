package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * An example how to draw lines into a NiftyCanvas.
 * @author void
 */
public class UseCase_b11_CanvasLines {
  private static final float Y_POS = 60.f;

  public UseCase_b11_CanvasLines(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    NiftyNode childNode = rootNode.newChildNode(UnitValue.px(512), UnitValue.px(512));
    childNode.startAnimatedRedraw(0, 16);
    childNode.addCanvasPainter(new NiftyCanvasPainter() {

      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        long time = nifty.getTimeProvider().getMsTime();

        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        // NiftyLineJoinType.Miter

        canvas.beginPath();
        addPath(canvas, time, Y_POS + 0 * 75.f);
        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Round);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(NiftyColor.WHITE());
        canvas.stroke();

        canvas.beginPath();
        addPath(canvas, time, Y_POS + 1 * 75.f);
        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Square);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(NiftyColor.RED());
        canvas.stroke();

        canvas.beginPath();
        addPath(canvas, time, Y_POS + 2 * 75.f);
        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Butt);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(NiftyColor.GREEN());
        canvas.stroke();
      }

      private void addPath(final NiftyCanvas canvas, long time, final float y) {
        canvas.moveTo( 10.f, y);
        canvas.lineTo(100.f, y + 50.f * (float) Math.sin(time / 900.));
        canvas.lineTo(200.f, y);
        canvas.lineTo(300.f, y + 50.f * (float) Math.sin(time / 750.));
        canvas.lineTo(400.f, y);
        canvas.lineTo(500.f, y + 50.f * (float) Math.sin(time / 650.));
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b11_CanvasLines.class, args);
  }
}
