package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.accessor.NiftyCanvasAccessor;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;

/**
 * This can be used to draw. The actual content a NiftyNode displays is a NiftyCanvas.
 * @author void
 */
public class NiftyCanvas {
  private final InternalNiftyCanvas impl;

  NiftyCanvas(final InternalNiftyCanvas impl) {
    this.impl = impl;
  }

  public void setFillStyle(final NiftyColor color) {
    impl.setFillColor(color);
  }

  public void setFillStyle(final NiftyLinearGradient gradient) {
  }

  public void fillRect(final double x0, final double y0, final double x1, final double y1) {
    impl.filledRect(x0, y0, x1, y1);
  }

  /*
  public void setStrokeColor(final NiftyColor color) {
    impl.setStrokeColor(color);
  }

  public void setLineWidth(final double lineWidth) {
    impl.setLineWidth(lineWidth);
  }

  public void line(final double x0, final double y0, final double x1, final double y1) {
    impl.line(x0, y0, x1, y1);
  }

  public void rect(final double x0, final double y0, final double x1, final double y1) {
    impl.rect(x0, y0, x1, y1);
  }

*/
  InternalNiftyCanvas getImpl() {
    return impl;
  }

  static {
    NiftyCanvasAccessor.DEFAULT = new InternalNiftyCanvasAccessorImpl();
  }
}
