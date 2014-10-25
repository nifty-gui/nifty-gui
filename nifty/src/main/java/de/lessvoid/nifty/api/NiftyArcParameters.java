package de.lessvoid.nifty.api;

/**
 * This class stores all of the properties necessary to render an arc.
 */
public class NiftyArcParameters {
  private final NiftyLineParameters lineParameters;
  private final float startAngle;
  private final float endAngle;
  private final float r;

  public NiftyArcParameters(final NiftyLineParameters lineParameters, final float startAngle, final float endAngle, final float r) {
    this.lineParameters = new NiftyLineParameters(lineParameters);
    this.startAngle = startAngle;
    this.endAngle = endAngle;
    this.r = r;
  }

  public NiftyArcParameters(final NiftyArcParameters arcParameters) {
    this.lineParameters = new NiftyLineParameters(arcParameters.lineParameters);
    this.startAngle = arcParameters.startAngle;
    this.endAngle = arcParameters.endAngle;
    this.r = arcParameters.r;
  }

  public NiftyLineParameters getLineParameters() {
    return lineParameters;
  }

  public float getStartAngle() {
    return startAngle;
  }

  public float getEndAngle() {
    return endAngle;
  }

  public float getRadius() {
    return r;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(endAngle);
    result = prime * result + ((lineParameters == null) ? 0 : lineParameters.hashCode());
    result = prime * result + Float.floatToIntBits(r);
    result = prime * result + Float.floatToIntBits(startAngle);
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
    NiftyArcParameters other = (NiftyArcParameters) obj;
    if (Float.floatToIntBits(endAngle) != Float.floatToIntBits(other.endAngle))
      return false;
    if (lineParameters == null) {
      if (other.lineParameters != null)
        return false;
    } else if (!lineParameters.equals(other.lineParameters))
      return false;
    if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r))
      return false;
    if (Float.floatToIntBits(startAngle) != Float.floatToIntBits(other.startAngle))
      return false;
    return true;
  }
}