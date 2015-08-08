/*
 * Copyright (c) 2015, Nifty GUI Community 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.api.canvas;

import de.lessvoid.nifty.api.*;
import de.lessvoid.nifty.api.types.*;
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

  // colors, styles and shadows

  public void setFillStyle(final NiftyColor color) {
    impl.setFillColor(color);
  }

  public void setFillStyle(final NiftyLinearGradient gradient) {
    impl.setFillLinearGradient(gradient);
  }

  public void setStrokeColor(final NiftyColor color) {
    impl.setStrokeColor(color);
  }

  public void setStrokeColor(final NiftyLinearGradient gradient) {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void setShadowColor(final NiftyColor color) {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void setShadowBlur(final double shadowBlur) {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void setShadowOffsetX(final double xOffset) {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void setShadowOffsetY(final double yOffset) {
    // TODO
    throw new UnsupportedOperationException();
  }

  // line styles

  public void setLineCap(final NiftyLineCapType lineCapType) {
    impl.setLineCap(lineCapType);
  }

  public void setLineJoin(final NiftyLineJoinType lineJoinType) {
    impl.setLineJoin(lineJoinType);
  }

  public void setLineWidth(final double lineWidth) {
    impl.setLineWidth(lineWidth);
  }

  public void setMiterLimit(final double miterLength) {
    // TODO
    throw new UnsupportedOperationException();
  }

  // rectangles

  public void rect(final double x0, final double y0, final double x1, final double y1) {
    // TODO
    throw new UnsupportedOperationException();    
  }

  public void fillRect(final double x, final double y, final double width, final double height) {
    impl.filledRect(x, y, width, height);
  }

  public void strokeRect(final double x0, final double y0, final double x1, final double y1) {
    // TODO
    throw new UnsupportedOperationException();    
  }

  public void clearRect(final double x0, final double y0, final double x1, final double y1) {
    // TODO
    throw new UnsupportedOperationException();    
  }

  // paths

  public void fill() {
    impl.fillPath();
  }

  public void stroke() {
    impl.stroke();
  }

  public void beginPath() {
    impl.beginPath();
  }

  public void moveTo(final double x, final double y) {
    impl.moveTo(x, y);
  }

  public void closePath() {
    impl.closePath();
  }

  public void lineTo(final double x, final double y) {
    impl.lineTo(x, y);
  }

  public void clip() {
    // TODO
    throw new UnsupportedOperationException();    
  }

  public void quadraticCurveTo(final double cp1x, final double cp1y, final double x, final double y) {
    // TODO
    throw new UnsupportedOperationException();    
  }

  /**
   * The bezierCurveTo() method adds a point to the current path by using the specified control points that represent
   * a cubic Bezier curve.
   *
   * A cubic bezier curve requires three points. The first two points are control points that are used in the cubic
   * Bezier calculation and the last point is the ending point for the curve. The starting point for the curve is the
   * last point in the current path. If a path does not exist, use the beginPath() and moveTo() methods to define a
   * starting point.
   *
   * @param cp1x control point 1 x
   * @param cp1y control point 1 y
   * @param cp2x control point 2 x
   * @param cp2y control point 2 y
   * @param x end point x
   * @param y end point y
   */
  public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y, final double x, final double y) {
    impl.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
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

  public void arcTo(final double x1, final double y1, final double x2, final double y2, final double r) {
    // TODO
    throw new UnsupportedOperationException();    
  }

  public boolean isPointInPath() {
    // TODO
    throw new UnsupportedOperationException();    
  }

  // transformations

  public void scale(final double scaleWidth, final double scaleHeight) {
    impl.scale(scaleWidth, scaleHeight);
  }

  public void rotateRadians(final double angleRadians) {
    impl.rotate(angleRadians * 180 / Math.PI);
  }

  public void rotateDegrees(final double angleDegree) {
    impl.rotate(angleDegree);
  }

  public void translate(final double translateX, final double translateY) {
    impl.translate(translateX, translateY);
  }

  public void transform(final double a, final double b, final double c, final double d, final double e, final double f) {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void setTransform() {
    impl.resetTransform();
  }

  // text - this currently uses bitmap texts so the API is a bit different until we support True/Open Type

  public void setTextColor(final NiftyColor textColor) {
    impl.setTextColor(textColor);
  }

  public void setTextSize(final double textSize) {
    impl.setTextSize(textSize);
  }

  public void text(final NiftyFont niftyFont, final int x, final int y, final String text) {
    impl.text(niftyFont, x, y, text);
  }

  // image drawing

  public void drawImage(final NiftyImage image, final int x, final int y) {
    impl.image(x, y, image);
  }

  // compositing

  public void globalAlpha(final double globalAlpha) {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void setGlobalCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    impl.setGlobalCompositeOperation(compositeOperation);
  }

  // internals

  InternalNiftyCanvas getImpl() {
    return impl;
  }

  static {
    NiftyCanvasAccessor.DEFAULT = new InternalNiftyCanvasAccessorImpl();
  }
}
