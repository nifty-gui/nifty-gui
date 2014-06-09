package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class Context {
  private final NiftyTexture texture;
  private NiftyColor fillColor;
  private float lineWidth;
  private NiftyLinearGradient linearGradient;
  private NiftyColor strokeStyle;

  public Context(final NiftyTexture textureParam) {
    texture = textureParam;
  }

  public void prepare(final NiftyRenderDevice renderDevice) {
    fillColor = NiftyColor.BLACK();
    linearGradient = null;
    renderDevice.beginRenderToTexture(texture);
  }

  public void flush(final NiftyRenderDevice renderDevice) {
    renderDevice.endRenderToTexture(texture);
  }

  public void setFillColor(final NiftyColor color) {
    fillColor = new NiftyColor(color);
    linearGradient = null;
  }

  public NiftyColor getFillColor() {
    return fillColor;
  }

  public void setFillLinearGradient(final NiftyLinearGradient gradient) {
    fillColor = null;
    linearGradient = new NiftyLinearGradient(gradient);
  }

  public void setLineWidth(final float lineWidth) {
    this.lineWidth = lineWidth;
  }

  public float getLineWidth() {
    return lineWidth;
  }

  public NiftyTexture getNiftyTexture() {
    return texture;
  }

  public NiftyLinearGradient getFillLinearGradient() {
    return linearGradient;
  }

  public void setStrokeStyle(final NiftyColor color) {
    this.strokeStyle = color;
  }

  public NiftyColor getStrokeStyle() {
    return strokeStyle;
  }
}
