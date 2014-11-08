package de.lessvoid.nifty.api;


/**
 * A NiftyPoint is just an x and y coordinate.
 * @author void
 */
public class NiftyPoint {
  private final float x;
  private final float y;

  public NiftyPoint(final float x, final float y) {
    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public String toString() {
    return x + ", " + y;
  }
}
