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
    niftyNode.setBackgroundColor(NiftyColor.GREEN());

    child = niftyNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50));
    child.setContent(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        // fill the whole node content with a plain white color
        canvas.setFillStyle(NiftyColor.WHITE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        // create a funky black rectangle inside the white
        canvas.setFillStyle(NiftyColor.BLACK());
        canvas.fillRect(
            node.getWidth() / 2 - Math.random() * node.getWidth() / 2,
            node.getHeight() / 2 - Math.random() * node.getHeight() / 2,
            node.getWidth() - node.getWidth() / 2 + Math.random() * node.getWidth() / 2,
            node.getHeight() - node.getHeight() / 2 + Math.random() * node.getHeight() / 2);
      }
    });
  }

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    totalTime += deltaTime;

    // force a redraw of the canvas every 50ms
    if (totalTime > 50) {
      child.requestRedraw();
      totalTime = 0;
    }
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_0007_Canvas.class, args);
  }
}
