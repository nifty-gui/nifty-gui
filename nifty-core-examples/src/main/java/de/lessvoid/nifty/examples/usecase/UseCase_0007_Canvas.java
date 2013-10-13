package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;
import de.lessvoid.nifty.api.UnitValue;

/**
 * Test a custom canvas painter.
 * @author void
 */
public class UseCase_0007_Canvas implements UseCaseUpdateable {
  private final NiftyNode niftyNode;
  private final NiftyNode child;
  private float totalTime;

  public UseCase_0007_Canvas(final Nifty nifty) {
    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());

    child = niftyNode.newChildNode(UnitValue.percent(100), UnitValue.percent(100));
    child.setBackgroundColor(NiftyColor.RED());
    child.setContent(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
//        canvas.setFillColor(NiftyColor.WHITE());
//        canvas.setLineWidth(10.f);
//        canvas.line(0, Math.random() * node.getHeight(), node.getWidth(), Math.random() * node.getHeight());
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_0007_Canvas.class, args);
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    totalTime += deltaTime;

    if (totalTime > 25) {
      child.requestRedraw();
      totalTime = 0;
    }
  }
}
