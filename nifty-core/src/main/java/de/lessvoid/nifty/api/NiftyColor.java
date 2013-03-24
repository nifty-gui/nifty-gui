package de.lessvoid.nifty.api;

import java.util.Random;

import de.lessvoid.nifty.internal.NiftyColorStringParser;

/**
 * Immutable Color representation for Nifty. Nifty uses this class when color information is needed or returned by
 * a method. A mutable Color representation is available in a separate class @see NftyMutableColor.
 *
 * @author void
 */
public class NiftyColor {
  /**
   * A helper class to parse color Strings.
   */
  private static final NiftyColorStringParser parser = new NiftyColorStringParser();

  /**
   * red component.
   */
  private final double red;

  /**
   * green component.
   */
  private final double green;

  /**
   * blue component.
   */
  private final double blue;

  /**
   * alpha component.
   */
  private final double alpha;

  /**
   * Create a color from components.
   * @param newRed red component
   * @param newGreen green component
   * @param newBlue blue component
   * @param newAlpha alpha component
   */
  public NiftyColor(final double newRed, final double newGreen, final double newBlue, final double newAlpha) {
      this.red = newRed;
      this.green = newGreen;
      this.blue = newBlue;
      this.alpha = newAlpha;
  }

  /**
   * Copy constructor.
   * @param color the other color
   * @return NiftyColor
   */
  public NiftyColor(final NiftyColor color) {
    this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  /**
   * Get a new BLACK color.
   * @return NiftyColor
   */
  public static NiftyColor BLACK() {
    return new NiftyColor(0.0f, 0.0f, 0.0f, 1.0f);
  }

  /**
   * Get a new WHITE color.
   * @return NiftyColor
   */
  public static NiftyColor WHITE() {
    return new NiftyColor(1.0f, 1.0f, 1.0f, 1.0f);
  }

  /**
   * Get a new RED color.
   * @return NiftyColor
   */
  public static NiftyColor RED() {
    return new NiftyColor(1.0f, 0.0f, 0.0f, 1.0f);
  }

  /**
   * Get a new GREEN color.
   * @return NiftyColor
   */
  public static NiftyColor GREEN() {
    return new NiftyColor(0.0f, 1.0f, 0.0f, 1.0f);
  }

  /**
   * Get a new BLUE color.
   * @return NiftyColor
   */
  public static NiftyColor BLUE() {
    return new NiftyColor(0.0f, 0.0f, 1.0f, 1.0f);
  }

  /**
   * Get a new YELLOW color.
   * @return NiftyColor
   */
  public static NiftyColor YELLOW() {
    return new NiftyColor(1.0f, 1.0f, 0.0f, 1.0f);
  }

  /**
   * Get a new fully transparent black color.
   * @return NiftyColor
   */
  public static NiftyColor NONE() {
    return new NiftyColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  /**
   * Get a new fully transparent color.
   * @return NiftyColor
   */
  public static NiftyColor TRANSPARENT() {
    return new NiftyColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  /**
   * Create a color from a color String formated like in HTML but with alpha, e.g.: "#ff00ffff" or "#f0ff"
   * @param color the color string
   */
  public static NiftyColor fromString(final String color) {
    return parser.fromString(color);
  }

  /**
   * Create a color from another color, using the given alpha value.
   * @param color color
   * @param alpha alpha component
   */
  public static NiftyColor fromColorWithAlpha(final NiftyColor color, final double alpha) {
      return new NiftyColor(color.getRed(), color.getGreen(), color.getBlue(), alpha);
  }

  /**
   * Create a color from an encoded int value R + G + B + Alpha.
   * @param color color value
   */
  public static NiftyColor fromInt(final int color) {
    int red = (color >> 24) & 0xFF;
    int green = (color >> 16) & 0xFF;
    int blue = (color >> 8) & 0xFF;
    int alpha = (color >> 0) & 0xFF;
    return new NiftyColor(red / 255.f, green / 255.f, blue / 255.f, alpha / 255.f);
  }

  /**
   * Create a color from Hue, Saturation and Value values. With Hue in [0, 360] and Saturation and Value [0, 1].
   * @return NiftyColor
   */
  public static NiftyColor fromHSV(final double hue, final double saturation, final double value) {
    int h_i = ((int)(hue/60)) % 6;
    double f = (hue/60) - (int)(hue/60);

    double red = 0;
    double green = 0;
    double blue = 0;

    double p = (value * (1.0 - saturation));
    double q = (value * (1.0 - f*saturation));
    double t = (value * (1.0 - saturation * (1.0 - f)));

    switch(h_i) {
      case 0:  red = value; green = t; blue = p; break;
      case 1:  red = q; green = value; blue = p; break;
      case 2:  red = p; green = value; blue = t; break;
      case 3:  red = p; green = q; blue = value; break;
      case 4:  red = t; green = p; blue = value; break;
      case 5:  red = value; green = p; blue = q; break;
    }

    return new NiftyColor(red, green, blue, 1.0f);
  }

  /**
   * Create a random Color with full Alpha (1.0f)
   * @return a color with random red, green and blue components
   */
  public static NiftyColor randomColor() {
    Random random = new Random();
    return new NiftyColor(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1.f);
  }

  /**
   * Get red component.
   * @return red component
   */
  public double getRed() {
      return red;
  }

  /**
   * Get green component.
   * @return green component
   */
  public double getGreen() {
      return green;
  }

  /**
   * Get blue component.
   * @return blue component
   */
  public double getBlue() {
      return blue;
  }

  /**
   * Get alpha component.
   * @return alpha component
   */
  public double getAlpha() {
      return alpha;
  }

  /**
   * Return a string representation of this color.
   */
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("(").append(red).append(",")
          .append(green).append(",")
          .append(blue).append(",")
          .append(alpha).append(")");
    return result.toString();
  }


  /**
   * @see Object#hashCode() hashCode
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(alpha);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(blue);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(green);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(red);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /**
   * @see Object#equals(Object) equals
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NiftyColor other = (NiftyColor) obj;
    if (Double.doubleToLongBits(alpha) != Double.doubleToLongBits(other.alpha))
      return false;
    if (Double.doubleToLongBits(blue) != Double.doubleToLongBits(other.blue))
      return false;
    if (Double.doubleToLongBits(green) != Double.doubleToLongBits(other.green))
      return false;
    if (Double.doubleToLongBits(red) != Double.doubleToLongBits(other.red))
      return false;
    return true;
  }
}
