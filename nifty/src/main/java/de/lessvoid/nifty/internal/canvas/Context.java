package de.lessvoid.nifty.internal.canvas;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice.LineParameters;
import de.lessvoid.nifty.spi.NiftyTexture;

public class Context {
  private final NiftyTexture texture;
  private NiftyColor fillColor;
  private NiftyLinearGradient linearGradient;
  private LineParameters lineParameters;
  private NiftyColor textColor = NiftyColor.WHITE();
  private float textSize = 1.f;
  private Mat4 transform = Mat4.createIdentity();
  private List<PathElement> path = new ArrayList<PathElement>();
  private float currentPathX = 0.f;
  private float currentPathY = 0.f;
  private float currentPathStartX = 0.f;
  private float currentPathStartY = 0.f;

  public Context(final NiftyTexture textureParam) {
    texture = textureParam;
  }

  public void prepare(final NiftyRenderDevice renderDevice) {
    fillColor = NiftyColor.BLACK();
    linearGradient = null;
    lineParameters = new LineParameters();
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
    lineParameters.setLineWidth(lineWidth);
  }

  public float getLineWidth() {
    return lineParameters.getLineWidth();
  }

  public NiftyTexture getNiftyTexture() {
    return texture;
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

  public void setTextSize(final float textSize) {
    this.textSize = textSize;
  }

  public float getTextSize() {
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
  }

  public void moveTo(final float x, final float y) {
    currentPathX = x;
    currentPathY = y;

    if (path.isEmpty()) {
      currentPathStartX = x;
      currentPathStartY = y;
    }
  }

  public void lineTo(final float x, final float y) {
    path.add(new PathElementLine(currentPathX, currentPathY, x, y));
    moveTo(x, y);
  }

  public void closePath() {
    lineTo(currentPathStartX, currentPathStartY);
  }

  public void strokePath(final BatchManager batchManager) {
    for (int i=0; i<path.size(); i++) {
      path.get(i).render(batchManager, lineParameters, i == 0);
    }
  }

  interface PathElement {
    void render(BatchManager batchManager, LineParameters lineParameters, final boolean first);
  }

  class PathElementLine implements PathElement {
    private float x0;
    private float y0;
    private float x1;
    private float y1;

    public PathElementLine(final float x0, final float y0, final float x1, final float y1) {
      this.x0 = x0;
      this.y0 = y0;
      this.x1 = x1;
      this.y1 = y1;
    }

    @Override
    public void render(final BatchManager batchManager, final LineParameters lineParameters, final boolean first) {
      batchManager.addLine(x0, y0, x1, y1, getTransform(), lineParameters, first);
    }
  }
}
