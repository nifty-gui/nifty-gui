package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyNode;



public class InternalNiftyCanvasPainterStandard implements NiftyCanvasPainter {

  @Override
  public void paint(final NiftyNode node, final NiftyCanvas canvas) {
    canvas.setFillStyle(node.getBackgroundColor());
    canvas.fillRect(0, 0, node.getWidth(), node.getHeight());
  }
}
