package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
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
    niftyNode.setBackgroundColor(NiftyColor.TRANSPARENT());

    child = niftyNode.newChildNode(UnitValue.percent(100), UnitValue.percent(100));
    child.setBackgroundColor(NiftyColor.RED());
    child.setContent(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.WHITE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setFillStyle(NiftyColor.randomColor());
        canvas.fillRect(50, 50, 400, 150);

      }
    });
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    totalTime += deltaTime;

    if (totalTime > 5) {
      child.requestRedraw();
      totalTime = 0;
    }
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_0007_Canvas.class, args);
  }
}
