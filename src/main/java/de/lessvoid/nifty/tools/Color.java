package de.lessvoid.nifty.tools;

/**
 * Color helper class to manage colors.
 * @author void
 */
public class Color {

  /**
   * scale short mode factor (converts 0x5 to 0x55).
   */
  private static final int SCALE_SHORT_MODE = 0x11;

  /**
   * the default empty color.
   */
  public static final Color NONE = new Color(0.0f, 0.0f, 0.0f, 0.0f);

  /**
   * a white color.
   */
  public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);

  /**
   * a black color.
   */
  public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);

  /**
   * max value for conversion.
   */
  private static final float MAX_INT_VALUE = 255.0f;

  /**
   * hex base to convert numbers.
   */
  private static final int HEX_BASE = 16;

  /**
   * red component.
   */
  private float red = 0.0f;

  /**
   * green component.
   */
  private float green = 0.0f;

  /**
   * blue component.
   */
  private float blue = 0.0f;

  /**
   * alpha component.
   */
  private float alpha = 0.0f;

  /**
   * Create a color from a color String formated like in html
   * code but with alpha, e.g.: "#ff00ffff".
   * @param color the color string
   */
  public Color(final String color) {
      this.red = getRFromString(color);
      this.green = getGFromString(color);
      this.blue = getBFromString(color);
      this.alpha = getAFromString(color);
  }

  /**
   * Create a color from components.
   * @param newRed red component
   * @param newGreen green component
   * @param newBlue blue component
   * @param newAlpha alpha component
   */
  public Color(
          final float newRed,
          final float newGreen,
          final float newBlue,
          final float newAlpha) {
      this.red = newRed;
      this.green = newGreen;
      this.blue = newBlue;
      this.alpha = newAlpha;
  }

  /**
   * linear interpolate between this color and the given color.
   * @param end end color
   * @param t t in [0,1]
   * @return linear interpolated color
   */
  public final Color linear(final Color end, final float t) {
      return new Color(
              this.red + t * (end.red - this.red),
              this.green + t * (end.green - this.green),
              this.blue + t * (end.blue - this.blue),
              this.alpha + t * (end.alpha - this.alpha));
  }

  /**
   * get the red component.
   * @return red
   */
  public final float getRed() {
      return red;
  }

  /**
   * get the green component.
   * @return green
   */
  public final float getGreen() {
      return green;
  }

  /**
   * get the blue component.
   * @return blue
   */
  public final float getBlue() {
      return blue;
  }

  /**
   * get alpha value.
   * @return alpha
   */
  public final float getAlpha() {
      return alpha;
  }

  /**
   * helper to get red from a string value.
   * @param color color string
   * @return extracted red value
   */
  private float getRFromString(final String color) {
    if (isShortMode(color)) {
      return (Integer.valueOf(color.substring(1, 2), HEX_BASE) * SCALE_SHORT_MODE) / MAX_INT_VALUE;
    } else {
      return Integer.valueOf(color.substring(1, 3), HEX_BASE) / MAX_INT_VALUE;
    }
  }

  /**
   * helper to get green from a string value.
   * @param color color string
   * @return extracted green
   */
  private float getGFromString(final String color) {
    if (isShortMode(color)) {
      return (Integer.valueOf(color.substring(2, 3), HEX_BASE) * SCALE_SHORT_MODE) / MAX_INT_VALUE;
    } else {
      return Integer.valueOf(color.substring(3, 5), HEX_BASE) / MAX_INT_VALUE;
    }
  }

  /**
   * helper to get blue from a string value.
   * @param color color string
   * @return extracted blue
   */
  private float getBFromString(final String color) {
    if (isShortMode(color)) {
      return (Integer.valueOf(color.substring(3, 4), HEX_BASE) * SCALE_SHORT_MODE) / MAX_INT_VALUE;
    } else {
      return Integer.valueOf(color.substring(5, 7), HEX_BASE) / MAX_INT_VALUE;
    }
  }

  /**
   * helper to get alpha from a string value.
   * @param color color string
   * @return alpha value
   */
  private float getAFromString(final String color) {
    if (isShortMode(color)) {
      return (Integer.valueOf(color.substring(4, 5), HEX_BASE) * SCALE_SHORT_MODE) / MAX_INT_VALUE;
    } else {
      return Integer.valueOf(color.substring(7, 9), HEX_BASE) / MAX_INT_VALUE;
    }
  }

  /**
   * Returns true when the given string is from format: #ffff and false when #ffffffff.
   * @param color the color string
   * @return true or false
   */
  private boolean isShortMode(final String color) {
    return color.length() == 5;
  }

  /**
   * Multiply all components with the given factor.
   * @param factor factor to multiply
   * @return new Color with factor applied
   */
  public Color mutiply(final float factor) {
    return new Color(
        red * factor,
        green * factor,
        blue * factor,
        alpha * factor);
  }
  
  public String toString() {
    return red + ":" + green + ":" + blue + ":" + alpha;
  }
}
