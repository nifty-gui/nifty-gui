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
    impl.setFillLinearGradient(gradient);
  }

  public void fillRect(final double x0, final double y0, final double x1, final double y1) {
    impl.filledRect(x0, y0, x1, y1);
  }

  public void setStrokeColor(final NiftyColor color) {
    impl.setStrokeColor(color);
  }

  public void setLineWidth(final double lineWidth) {
    impl.setLineWidth(lineWidth);
  }

  public void setLineCap(final NiftyLineCapType lineCapType) {
    impl.setLineCap(lineCapType);
  }

  public void setLineJoin(final NiftyLineJoinType lineJoinType) {
    impl.setLineJoin(lineJoinType);
  }

  /**
   * The arc() method creates an arc/curve (used to create circles, or parts of circles).
   *
   * @param x The x-coordinate of the center of the circle
   * @param y The y-coordinate of the center of the circle
   * @param r The radius of the circle
   * @param startAngle The starting angle, in radians (0 is at 3 o'clock position of the arc's circle)
   * @param endAngle The ending angle, in radians.
   */
  public void arc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    impl.arc(x, y, r, startAngle, endAngle);
  }

  public void setTextColor(final NiftyColor textColor) {
    impl.setTextColor(textColor);
  }

  public void setTextSize(final float textSize) {
    impl.setTextSize(textSize);
  }

  public void text(final NiftyFont niftyFont, final int x, final int y, final String text) {
    impl.text(niftyFont, x, y, text);
  }

  public void image(final NiftyImage image, final int x, final int y) {
    impl.image(x, y, image);
  }

  public void scale(final float scaleWidth, final float scaleHeight) {
    impl.scale(scaleWidth, scaleHeight);
  }

  public void rotateRadians(final float angleRadians) {
    impl.rotate((float) (angleRadians * 180 / Math.PI));
  }

  public void rotateDegrees(final float angleDegree) {
    impl.rotate(angleDegree);
  }

  public void translate(final float translateX, final float translateY) {
    impl.translate(translateX, translateY);
  }

  public void resetTransform() {
    impl.resetTransform();
  }

  public void beginPath() {
    impl.beginPath();
  }

  public void closePath() {
    impl.closePath();
  }

  public void moveTo(final float x, final float y) {
    impl.moveTo(x, y);
  }

  public void lineTo(final float x, final float y) {
    impl.lineTo(x, y);
  }

  public void stroke() {
    impl.stroke();
  }
  /*


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
