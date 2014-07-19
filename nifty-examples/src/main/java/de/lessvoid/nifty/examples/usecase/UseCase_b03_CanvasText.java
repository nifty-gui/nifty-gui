package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * custom canvas painter rendering text
 * @author void
 */
public class UseCase_b03_CanvasText {
  private final NiftyNode niftyNode;

  public UseCase_b03_CanvasText(final Nifty nifty) throws IOException {
    final NiftyFont font = nifty.createFont("fonts/verdana-small-regular.fnt");

    niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setTextColor(NiftyColor.WHITE());
        canvas.text(font, 10, 10, "abcdefghijklmnopqrstuvwxyz");

        canvas.setTextColor(NiftyColor.RED());
        canvas.setTextSize(1.2f);
        canvas.text(font, 10, 40, "abcdefghijklmnopqrstuvwxyz");

        canvas.setTextColor(NiftyColor.GREEN());
        canvas.setTextSize(1.4f);
        canvas.text(font, 10, 70, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        canvas.setTextColor(NiftyColor.YELLOW());
        canvas.setTextSize(1.6f);
        canvas.text(font, 10, 100, "0123456789");

        canvas.setTextColor(NiftyColor.WHITE());
        canvas.setTextSize(2.0f);
        canvas.text(font, 10, 150, "Hello Nifty 2.0 Text");
}
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b03_CanvasText.class, args);
  }
}
