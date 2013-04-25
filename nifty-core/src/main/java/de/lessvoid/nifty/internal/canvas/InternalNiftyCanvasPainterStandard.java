package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.InternalNiftyNode;



public class InternalNiftyCanvasPainterStandard {

  public void paint(final InternalNiftyNode node, final InternalNiftyCanvas canvas) {
    canvas.reset();
    canvas.setFillColor(node.getBackgroundColor());
    canvas.filledRect(0, 0, node.getWidth(), node.getHeight());
  }
}
