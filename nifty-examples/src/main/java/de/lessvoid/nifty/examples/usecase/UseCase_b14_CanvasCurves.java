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

  ControlPoint cp0;
  ControlPoint cp1;
  ControlPoint cp2;
  ControlPoint cp3;

  private static class ControlPoint {
    private float x;
    private float y;
    private NiftyNode handle;

    public ControlPoint(final float x, final float y, final NiftyNode parent, final NiftyColor color) {
      this.x = x;
      this.y = y;

      handle = parent.newChildNode(UnitValue.px(12), UnitValue.px(12));
      handle.addCanvasPainter(new NiftyCanvasPainter() {
        @Override
        public void paint(final NiftyNode node, final NiftyCanvas canvas) {
          canvas.setStrokeColor(color);
          canvas.setLineWidth(1.f);
          canvas.arc(node.getWidth() / 2.f, node.getHeight() / 2.f, node.getWidth() / 2.f, 0, 2*Math.PI);
          canvas.stroke();
        }
      });
      handle.setXConstraint(UnitValue.px((int) x - handle.getWidth() / 2));
      handle.setYConstraint(UnitValue.px((int) y - handle.getHeight() / 2));
    }

    public float getX() {
      return x;
    }

    public float getY() {
      return y;
    }
  }

  public UseCase_b14_CanvasCurves(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    NiftyNode canvasNode = rootNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Absolute);

    cp0 = new ControlPoint( 50.f,  50.f, canvasNode, NiftyColor.BLACK());
    cp1 = new ControlPoint( 50.f, 150.f, canvasNode, NiftyColor.BLACK());
    cp2 = new ControlPoint(250.f, 150.f, canvasNode, NiftyColor.BLACK());
    cp3 = new ControlPoint(250.f,  50.f, canvasNode, NiftyColor.BLACK());

    canvasNode.addCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.WHITE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setStrokeColor(NiftyColor.BLACK());
        canvas.setLineWidth(5);
        canvas.beginPath();
        canvas.moveTo(cp0.getX(), cp0.getY());
        canvas.bezierCurveTo(cp1.getX(), cp1.getY(), cp2.getX(), cp2.getY(), cp3.getX(), cp3.getY());
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
