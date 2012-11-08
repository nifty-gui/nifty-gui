package de.lessvoid.nifty;

import java.util.Random;

/**
 * Mutable Color representation for the public Nifty API. Nifty uses this class when color information is needed or
 * returned by method.
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
   * Create a color from components.
   * @param newRed red component
   * @param newGreen green component
   * @param newBlue blue component
   * @param newAlpha alpha component
   */
  public NiftyColor(final float newRed, final float newGreen, final float newBlue, final float newAlpha) {
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
  public static NiftyColor fromColorWithAlpha(final NiftyColor color, final float alpha) {
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
  public static NiftyColor fromHSV(final float hue, final float saturation, final float value) {
    int h_i = ((int)(hue/60)) % 6;
    float f = (hue/60) - (int)(hue/60);

    float red = 0;
    float green = 0;
    float blue = 0;

    float p = (float) (value * (1.0 - saturation));
    float q = (float) (value * (1.0 - f*saturation));
    float t = (float) (value * (1.0 - saturation * (1.0 - f)));

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
    return new NiftyColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.f);
  }

  public float getRed() {
      return red;
  }

  public float getGreen() {
      return green;
  }

  public float getBlue() {
      return blue;
  }

  public float getAlpha() {
      return alpha;
  }

  public NiftyColor setRed(final float newRed) {
    red = newRed;
    return this;
  }

  public NiftyColor setGreen(final float newGreen) {
    green = newGreen;
    return this;
  }

  public NiftyColor setBlue(final float newBlue) {
    blue = newBlue;
    return this;
  }

  public NiftyColor setAlpha(final float newColorAlpha) {
    alpha = newColorAlpha;
    return this;
  }

  public NiftyColor update(final NiftyColor src) {
    setRed(src.getRed());
    setGreen(src.getGreen());
    setBlue(src.getBlue());
    setAlpha(src.getAlpha());
    return this;
  }

  public NiftyColor linear(final NiftyColor start, final NiftyColor end, final float t) {
    setRed(start.getRed() + t * (end.getRed() - start.getRed()));
    setGreen(start.getGreen() + t * (end.getGreen() - start.getGreen()));
    setBlue(start.getBlue() + t * (end.getBlue() - start.getBlue()));
    setAlpha(start.getAlpha() + t * (end.getAlpha() - start.getAlpha()));
    return this;
  }

  public NiftyColor mutiply(final float factor) {
    setRed(getRed() * factor);
    setGreen(getGreen() * factor);
    setBlue(getBlue() * factor);
    setAlpha(getAlpha() * factor);
    return this;
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("(").append(red).append(",")
          .append(green).append(",")
          .append(blue).append(",")
          .append(alpha).append(")");
    return result.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(alpha);
    result = prime * result + Float.floatToIntBits(blue);
    result = prime * result + Float.floatToIntBits(green);
    result = prime * result + Float.floatToIntBits(red);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    NiftyColor other = (NiftyColor) obj;
    if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha)) {
      return false;
    }
    if (Float.floatToIntBits(blue) != Float.floatToIntBits(other.blue)) {
      return false;
    }
    if (Float.floatToIntBits(green) != Float.floatToIntBits(other.green)) {
      return false;
    }
    if (Float.floatToIntBits(red) != Float.floatToIntBits(other.red)) {
      return false;
    }
    return true;
  }
}
