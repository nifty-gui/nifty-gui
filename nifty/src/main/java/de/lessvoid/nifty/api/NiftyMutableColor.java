package de.lessvoid.nifty.api;

/**
 * A Mutable color implementation used when colors will need to be animated.
 * @author void
 */
public class NiftyMutableColor {

  /**
   * red component.
   */
  private double red = 0.0;

  /**
   * green component.
   */
  private double green = 0.0;

  /**
   * blue component.
   */
  private double blue = 0.0;

  /**
   * alpha component.
   */
  private double alpha = 0.0;

  /**
   * Create a new {@link NiftyMutableColor} from an existing {@link NiftyColor}.
   *
   * @param color the NiftyColor to initialize the new instance
   * @return new {@link NiftyMutableColor}
   */
  public static NiftyMutableColor fromColor(final NiftyColor color) {
    return new NiftyMutableColor(color);
  }

  /**
   * Create a color from components.
   * @param newRed red component
   * @param newGreen green component
   * @param newBlue blue component
   * @param newAlpha alpha component
   */
  public NiftyMutableColor(final double newRed, final double newGreen, final double newBlue, final double newAlpha) {
      this.red = newRed;
      this.green = newGreen;
      this.blue = newBlue;
      this.alpha = newAlpha;
  }

  /**
   * Create a color from a immutable NiftyColor.
   * @param color the other color
   * @return NiftyColor
   */
  public NiftyMutableColor(final NiftyColor color) {
    this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  /**
   * Copy constructor.
   * @param color the other color
   * @return NiftyColor
   */
  public NiftyMutableColor(final NiftyMutableColor color) {
    this(color.red, color.green, color.blue, color.alpha);
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
   * Get this color as a NiftyColor.
   * @return the NiftyColor
   */
  public NiftyColor getColor() {
    return new NiftyColor(red, green, blue, alpha);
  }

  /**
   * Set red component.
   * @param newRed the red component
   * @return this
   */
  public NiftyMutableColor setRed(final double newRed) {
    red = newRed;
    return this;
  }

  /**
   * Set green component.
   * @param newGreen the green component
   * @return this
   */
  public NiftyMutableColor setGreen(final double newGreen) {
    green = newGreen;
    return this;
  }

  /**
   * Set blue component.
   * @param newBlue the blue component
   * @return this
   */
  public NiftyMutableColor setBlue(final double newBlue) {
    blue = newBlue;
    return this;
  }

  /**
   * Set alpha component.
   * @param newAlpha the alpha component
   * @return this
   */
  public NiftyMutableColor setAlpha(final double newColorAlpha) {
    alpha = newColorAlpha;
    return this;
  }

  /**
   * Change all components of this to values from the given color.
   * @param color the source color
   * @return this
   */
  public NiftyMutableColor set(final NiftyColor color) {
    this.red = color.getRed();
    this.green = color.getGreen();
    this.blue = color.getBlue();
    this.alpha = color.getAlpha();
    return this;
  }

  /**
   * Update all of the components of this color with the components of another NiftyColor.
   * @param src the NiftyColor to copy the components from
   * @return this
   */
  public NiftyMutableColor update(final NiftyColor src) {
    setRed(src.getRed());
    setGreen(src.getGreen());
    setBlue(src.getBlue());
    setAlpha(src.getAlpha());
    return this;
  }

  /**
   * Linear interpolate between the start color and the end color using the given t which needs to be in [0, 1]
   * @param start start color
   * @param end end color
   * @param t the parameter for the linear interpolation. If this value is 0 this will be set to start color and if it's
   * 1 this will be the end color.
   * @return this
   */
  public NiftyMutableColor linear(final NiftyColor start, final NiftyColor end, final double t) {
    setRed(start.getRed() + t * (end.getRed() - start.getRed()));
    setGreen(start.getGreen() + t * (end.getGreen() - start.getGreen()));
    setBlue(start.getBlue() + t * (end.getBlue() - start.getBlue()));
    setAlpha(start.getAlpha() + t * (end.getAlpha() - start.getAlpha()));
    return this;
  }

  /**
   * Multiply all of the color components by the given factor. This color is being changed.
   * @param factor the factor to apply
   * @return this
   */
  public NiftyMutableColor mutiply(final double factor) {
    setRed(getRed() * factor);
    setGreen(getGreen() * factor);
    setBlue(getBlue() * factor);
    setAlpha(getAlpha() * factor);
    return this;
  }
}
