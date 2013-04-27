package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;

public class Context {
  private NiftyColor fillColor;

  public Context() {
    reset();
  }

  public void reset() {
    fillColor = NiftyColor.WHITE();
  }

  public void setFillColor(final NiftyColor color) {
    fillColor = color;
  }

  public NiftyColor getFillColor() {
    return fillColor;
  }
}
