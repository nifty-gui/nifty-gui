/*
 * Copyright (c) 2014, Jens Hohmuth 
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
package de.lessvoid.nifty.internal.canvas;

import java.util.ArrayList;
import java.util.List;

import org.jglfont.JGLFont;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyCompositeOperation;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyImage;
import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.InternalNiftyImage;
import de.lessvoid.nifty.internal.accessor.NiftyFontAccessor;
import de.lessvoid.nifty.internal.accessor.NiftyImageAccessor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec2;
import de.lessvoid.nifty.internal.render.batch.BatchManager;
import de.lessvoid.nifty.internal.render.batch.ColorQuadBatch;
import de.lessvoid.nifty.internal.render.batch.TextureBatch;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;
import de.lessvoid.nifty.spi.parameter.NiftyLineParameters;

public class Context {
  // contentTexture is the final content this Context draws to - what you think of when you think the content
  private final NiftyTexture contentTexture;

  // everything we're rendering to this Context will first rendered into this texture and later composited into
  // the contentTexture
  private final NiftyTexture workingTexture;

  // we'll use the textureBatch to composite the workingTexture into the contentTexture
  private final TextureBatch textureBatch;

  // we'll use this ColorQuadBatch to clear the workingTexture
  private final ColorQuadBatch colorBatch;

  private NiftyColor fillColor;
  private NiftyLinearGradient linearGradient;
  private NiftyLineParameters lineParameters;
  private NiftyColor textColor = NiftyColor.WHITE();
  private double textSize = 1.f;
  private Mat4 transform = Mat4.createIdentity();
  private List<PathElement> path = new ArrayList<PathElement>();
  private Double currentPathX;
  private Double currentPathY;
  private Double currentPathStartX;
  private Double currentPathStartY;
  private NiftyRenderDevice renderDevice;
  private BatchManager batchManager;
  private NiftyCompositeOperation compositeOperation;

  public Context(final NiftyTexture contentTexture, final NiftyTexture workingTexture) {
    this.contentTexture = contentTexture;
    this.workingTexture = workingTexture;
    this.textureBatch = textureBatch(workingTexture);
    this.colorBatch = colorBatch();
  }

  public void bind(final NiftyRenderDevice renderDevice, final BatchManager batchManager) {
    this.renderDevice = renderDevice;
    this.batchManager = batchManager;
    this.compositeOperation = NiftyCompositeOperation.SourceOver;

    // start by cleaning the content texture
    renderDevice.beginRenderToTexture(contentTexture);
    renderDevice.changeCompositeOperation(NiftyCompositeOperation.Off);
    colorBatch.render(renderDevice);
    renderDevice.endRenderToTexture(workingTexture);
  }

  public void prepare() {
    batchManager.begin();

    fillColor = NiftyColor.BLACK();
    linearGradient = null;
    lineParameters = new NiftyLineParameters();
    renderDevice.beginRenderToTexture(workingTexture);
    renderDevice.changeCompositeOperation(NiftyCompositeOperation.Off);
    colorBatch.render(renderDevice);
    renderDevice.changeCompositeOperation(NiftyCompositeOperation.SourceOver);
  }

  public void flush() {
    batchManager.end(renderDevice);
    renderDevice.endRenderToTexture(workingTexture);

    // now render workingTexture into contentTexture
    renderDevice.beginRenderToTexture(contentTexture);
    renderDevice.changeCompositeOperation(compositeOperation);
    textureBatch.render(renderDevice);
    renderDevice.endRenderToTexture(contentTexture);

    // reset composite operation to the standard
    renderDevice.changeCompositeOperation(NiftyCompositeOperation.SourceOver);
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

  NiftyLineParameters getLineParameters() {
    return lineParameters;
  }

  public void setLineWidth(final float lineWidth) {
    lineParameters.setLineWidth(lineWidth);
  }

  public float getLineWidth() {
    return lineParameters.getLineWidth();
  }

  public NiftyTexture getNiftyTexture() {
    return contentTexture;
  }

  public NiftyLinearGradient getFillLinearGradient() {
    return linearGradient;
  }

  public void setStrokeStyle(final NiftyColor color) {
    this.lineParameters.setColor(color);
  }

  public void setLineCapType(final NiftyLineCapType lineCapType) {
    this.lineParameters.setLineCapType(lineCapType);
  }

  public void setLineJoinType(final NiftyLineJoinType lineJoinType) {
    this.lineParameters.setLineJoinType(lineJoinType);
  }

  public NiftyColor getStrokeStyle() {
    return lineParameters.getColor();
  }

  public void setTextSize(final double textSize) {
    this.textSize = textSize;
  }

  public double getTextSize() {
    return textSize;
  }

  public void setTextColor(final NiftyColor textColor) {
    this.textColor = textColor;
  }

  public NiftyColor getTextColor() {
    return textColor;
  }

  public void addTransform(final Mat4 mat) {
    transform = Mat4.mul(transform, mat);
  }

  public void resetTransform() {
    transform = Mat4.createIdentity();
  }

  public Mat4 getTransform() {
    return transform;
  }

  public void beginPath() {
    path.clear();
    currentPathX = null;
    currentPathY = null;
    currentPathStartX = null;
    currentPathStartY = null;
  }

  public void moveTo(final double x, final double y) {
    currentPathX = x;
    currentPathY = y;

    if (path.isEmpty()) {
      currentPathStartX = x;
      currentPathStartY = y;
    }
  }

  public void lineTo(final double x, final double y) {
    PathElement lastPathElement = null;
    if (!path.isEmpty()) {
      lastPathElement = path.get(path.size() - 1);
    }
    if (currentPathX != null && currentPathY != null && !(lastPathElement instanceof PathElementLine)) {
      path.add(new PathElementLine(currentPathX, currentPathY));
    }
    moveTo(x, y);
    path.add(new PathElementLine(currentPathX, currentPathY));
  }

  public void arc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    float startX = (float) (Math.cos(startAngle) * r + x);
    float startY = (float) (Math.sin(startAngle) * r + y);
    if (currentPathX != null && currentPathY != null) {
      path.add(new PathElementLine(currentPathX, currentPathY));
      path.add(new PathElementLine(startX, startY));
      moveTo(startX, startY);
    }

    path.add(new PathElementArc(x, y, r, startAngle, endAngle));

    float endX = (float) (Math.cos(endAngle) * r + x);
    float endY = (float) (Math.sin(endAngle) * r + y);
    moveTo(endX, endY);
  }

  public void closePath() {
    lineTo(currentPathStartX, currentPathStartY);
  }

  public void strokePath() {
    if (path.isEmpty()) {
      return;
    }
    for (int i=0; i<path.size(); i++) {
      path.get(i).render(this, batchManager, i == 0, i == (path.size() - 1));
    }
  }

  public void fillPath() {
  }

  public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y, final double x, final double y) {
    double startX = 0.f;
    double startY = 0.f;

    if (currentPathX != null &&
        currentPathY != null) {
      startX = currentPathStartX;
      startY = currentPathStartY;
    }

    CubicBezier curve = new CubicBezier(
        new Vec2((float) startX, (float) startY),
        new Vec2((float) cp1x, (float) cp1y),
        new Vec2((float) cp2x, (float) cp2y),
        new Vec2((float) x, (float) y));
    renderCurve(curve, this);
  }

  public void setCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    this.compositeOperation = compositeOperation;
  }

  public void addCustomShader(final String shaderId) {
    batchManager.addCustomShader(shaderId);
  }

  public void filledRect(final double x, final double y, final double width, final double height) {
    if (getFillLinearGradient() != null) {
      batchManager.addLinearGradientQuad(x, y, x + width, y + height, getTransform(), getFillLinearGradient());
      return;
    }
    batchManager.addColorQuad(x, y, x + width, y + height, getFillColor(), getTransform());
  }

  public void image(final int x, final int y, final NiftyImage image) {
    InternalNiftyImage internalImage = NiftyImageAccessor.getDefault().getInternalNiftyImage(image);
    Mat4 local = Mat4.mul(getTransform(), Mat4.createTranslate(x, y, 0.0f));
    batchManager.addTextureQuad(internalImage.getTexture(), local, NiftyColor.WHITE());
  }

  public void text(final int x, final int y, final NiftyFont font, final String text) {
    JGLFont jglFont = NiftyFontAccessor.getDefault().getJGLFont(font);
    jglFont.setCustomRenderState(batchManager);

    NiftyColor textColor = getTextColor();
    jglFont.renderText(
        x, y, text, (float) getTextSize(), (float) getTextSize(),
        (float) textColor.getRed(),
        (float) textColor.getGreen(),
        (float) textColor.getBlue(),
        (float) textColor.getAlpha());
  }

  private void renderCurve(final CubicBezier c, final Context context) {
    if (isSufficientlyFlat(c)) {
      c.output(context);
      return;
    }

    CubicBezier left = new CubicBezier();
    CubicBezier right = new CubicBezier();
    c.subdivide(left, right);

    renderCurve(left, context);
    renderCurve(right, context);
  }

  private boolean isSufficientlyFlat(final CubicBezier c) {
    double ux = 3.0*c.b1.x - 2.0*c.b0.x - c.b3.x; ux *= ux;
    double uy = 3.0*c.b1.y - 2.0*c.b0.y - c.b3.y; uy *= uy;
    double vx = 3.0*c.b2.x - 2.0*c.b3.x - c.b0.x; vx *= vx;
    double vy = 3.0*c.b2.y - 2.0*c.b3.y - c.b0.y; vy *= vy;
    if (ux < vx) ux = vx;
    if (uy < vy) uy = vy;
    double tol = .25;
    double tolerance = 16*tol*tol;
    return (ux+uy <= tolerance);
  }

  private TextureBatch textureBatch(final NiftyTexture texture) {
    TextureBatch result = new TextureBatch(texture);
    result.add(0., 0., texture.getWidth(), texture.getHeight(), 0., 0., 1., 1., Mat4.createIdentity(), NiftyColor.WHITE());
    return result;
  }

  private ColorQuadBatch colorBatch() {
    ColorQuadBatch result = new ColorQuadBatch();
    result.add(
        0.,
        0.,
        workingTexture.getWidth(),
        workingTexture.getHeight(),
        NiftyColor.TRANSPARENT(),
        NiftyColor.TRANSPARENT(),
        NiftyColor.TRANSPARENT(),
        NiftyColor.TRANSPARENT(),
        Mat4.createIdentity());
    return result;
  }
}
