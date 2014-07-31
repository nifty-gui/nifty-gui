package de.lessvoid.nifty.api;

/**
 * The DefaultNiftyCanvasPainter will be used when you don't set a specific one for a NiftyNode. It is part of the
 * public API so that you can use it as well (f.i. when you want the default behavior in your own NiftyCanvasPainter
 * as well).
 * 
 * @author void
 */
public class NiftyCanvasPainterDefault implements NiftyCanvasPainter {

  @Override
  public void paint(final NiftyNode node, final NiftyCanvas canvas) {
    canvas.setFillStyle(node.getBackgroundColor());
    canvas.fillRect(0, 0, node.getWidth(), node.getHeight());
  }
}
