package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyMutableColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * An example how to use an animated NiftyCanvas for a custom progress spinner animation.
 * @author void
 */
public class UseCase_b10_CanvasProgressSpinner {
  private int pos = 0;

  public UseCase_b10_CanvasProgressSpinner(final Nifty nifty) throws IOException {
    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);

    NiftyNode spinner = rootNode.newChildNode(UnitValue.px(128), UnitValue.px(128));
    spinner.addCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.resetTransform();
        canvas.setFillStyle(NiftyColor.BLACK());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        NiftyMutableColor color = NiftyMutableColor.fromColor(NiftyColor.fromString("#f00"));
        int max = 24;
        for (int i=0; i<max; i++) {
          int index = i + pos;
          if (index < 0) {
            index = index + max;
          }
          color.setAlpha(index / (float) max);
          canvas.resetTransform();
          canvas.translate(node.getWidth() / 2, node.getHeight() / 2);
          canvas.rotateDegrees(i * 360.f / (float) max);
          canvas.translate(25.f, 0.f);
          canvas.setFillStyle(color.getColor());
          canvas.fillRect(0, -2.5, node.getWidth() * 0.3, 2.5);
        }

        pos--;
        if (pos <= -max) {
          pos = 0;
        }
      }
    });
    spinner.startAnimatedRedraw(0, 50);
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b10_CanvasProgressSpinner.class, args);
  }
}
