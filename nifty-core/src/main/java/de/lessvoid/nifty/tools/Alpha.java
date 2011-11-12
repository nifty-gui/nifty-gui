package de.lessvoid.nifty.tools;

/**
 * Alpha helper class to manage alpha values.
 * @author void
 */
public class Alpha {
  public static final Alpha ZERO = new Alpha(0.0f);
  public static final Alpha FULL = new Alpha(1.0f);

  private static final int SCALE_SHORT_MODE = 0x11;
  private static final float MAX_INT_VALUE = 255.0f;
  private static final int HEX_BASE = 16;

  private float alpha = 0.0f;

  public Alpha(final String color) {
      this.alpha = getAFromString(color);
  }

  public Alpha(final float newAlpha) {
      this.alpha = newAlpha;
  }

  /**
   * linear interpolate between this color and the given color.
   * @param end end color
   * @param t t in [0,1]
   * @return linear interpolated color
   */
  public Alpha linear(final Alpha end, final float t) {
      return new Alpha(this.alpha + t * (end.alpha - this.alpha));
  }

  /**
   * get alpha value.
   * @return alpha
   */
  public final float getAlpha() {
      return alpha;
  }

  /**
   * helper to get alpha from a string value.
   * @param color color string
   * @return alpha value
   */
  private float getAFromString(final String color) {
    if (isShortMode(color)) {
      return (Integer.valueOf(color.substring(1, 2), HEX_BASE) * SCALE_SHORT_MODE) / MAX_INT_VALUE;
    } else {
      return Integer.valueOf(color.substring(1, 3), HEX_BASE) / MAX_INT_VALUE;
    }
  }

  /**
   * Returns true when the given string is from format: #ffff and false when #ffffffff.
   * @param color the color string
   * @return true or false
   */
  private boolean isShortMode(final String color) {
    return color.length() == 2;
  }

  /**
   * Multiply all components with the given factor.
   * @param factor factor to multiply
   * @return new Color with factor applied
   */
  public Alpha mutiply(final float factor) {
    return new Alpha(alpha * factor);
  }

  /**
   * convert color to string.
   * @return string representation
   */
  public String toString() {
    return "(" + alpha + ")";
  }

  /**
   * Set color alpha.
   * @param newColorAlpha new color alpha
   */
  public void setAlpha(final float newColorAlpha) {
    alpha = newColorAlpha;
  }
}
