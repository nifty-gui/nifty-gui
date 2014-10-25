package de.lessvoid.nifty.api;


/**
 * This collects all available parameters for rendering lines. This is part of the NiftyRenderDevice interface
 * because it is only part of the SPI and not really a part of the public Nifty API in the sense that this class
 * is only used to communicate settings between Nifty and the NiftyRenderDevice. This class is not meant to be used
 * by actual user code.
 */
public class NiftyLineParameters {
  private NiftyLineCapType lineCapType = NiftyLineCapType.Round;
  private NiftyLineJoinType lineJoinType = NiftyLineJoinType.Miter;
  private float lineWidth = 1.f;
  private NiftyColor lineColor = NiftyColor.WHITE();

  public NiftyLineParameters() {
  }

  public NiftyLineParameters(final NiftyLineParameters lineParameters) {
    lineCapType = lineParameters.getLineCapType();
    lineJoinType = lineParameters.getLineJoinType();
    lineWidth = lineParameters.getLineWidth();
    lineColor = lineParameters.getColor();
  }

  public NiftyLineCapType getLineCapType() {
    return lineCapType;
  }

  public void setLineCapType(final NiftyLineCapType lineCapType) {
    if (lineCapType == null) {
      throw new IllegalArgumentException("LineCapType can't be null");
    }
    this.lineCapType = lineCapType;
  }

  public NiftyLineJoinType getLineJoinType() {
    return lineJoinType;
  }

  public void setLineJoinType(final NiftyLineJoinType lineJoinType) {
    if (lineJoinType == null) {
      throw new IllegalArgumentException("LineJoinType can't be null");
    }
    this.lineJoinType = lineJoinType;
  }

  public float getLineWidth() {
    return lineWidth;
  }

  public void setLineWidth(float lineWidth) {
    this.lineWidth = lineWidth;
  }

  public void setColor(final NiftyColor color) {
    this.lineColor = color;
  }

  public NiftyColor getColor() {
    return lineColor;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((lineCapType == null) ? 0 : lineCapType.hashCode());
    result = prime * result + ((lineColor == null) ? 0 : lineColor.hashCode());
    result = prime * result + ((lineJoinType == null) ? 0 : lineJoinType.hashCode());
    result = prime * result + Float.floatToIntBits(lineWidth);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NiftyLineParameters other = (NiftyLineParameters) obj;
    if (lineCapType != other.lineCapType)
      return false;
    if (lineColor == null) {
      if (other.lineColor != null)
        return false;
    } else if (!lineColor.equals(other.lineColor))
      return false;
    if (lineJoinType != other.lineJoinType)
      return false;
    if (Float.floatToIntBits(lineWidth) != Float.floatToIntBits(other.lineWidth))
      return false;
    return true;
  }
}