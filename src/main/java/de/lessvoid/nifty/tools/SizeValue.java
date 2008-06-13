package de.lessvoid.nifty.tools;


/**
 * The SizeValue class stores and manages size value strings. Such strings
 * are used to store size representations. See the constants for all
 * supported special kind of values.
 *
 * @author void
 */
public class SizeValue {

  /**
   * Add a PIXEL to some size value to indicate a pixel value.
   * Example: "100px" or "640px"
   */
  private static final String PIXEL = "px";

  /**
   * Add a PERCENT to some size value to indicate a percent value.
   * Example: "100%" or "50%"
   */
  private static final String PERCENT = "%";

  /**
   * Max percent constant.
   */
  private static final float MAX_PERCENT = 100.0f;

  /**
   * The current value that has been set.
   */
  private String value;

  /**
   * percent value.
   */
  private float percentValue;

  /**
   * pixel value.
   */
  private float pixelValue;

  /**
   * Create a new instance using the given value.
   * @param valueParam the String value
   */
  public SizeValue(final String valueParam) {
    this.value = valueParam;
    this.percentValue = getPercentValue();
    this.pixelValue = getPixelValue();
  }

  /**
   * Checks if the value contains either PERCENT or PIXEL.
   * @return true when either PERCENT or PIXEL is given.
   */
  public final boolean isPercentOrPixel() {
    return isPercent() || isPixel();
  }

  /**
   * Get the value as float.
   * @param range the size that percent values are calculated from.
   * @return the result value as float
   */
  public final float getValue(final float range) {
    if (isPercent()) {
      return (range / MAX_PERCENT) * percentValue;
    } else if (isPixel()) {
      return pixelValue;
    } else {
      return -1;
    }
  }

  /**
   * Get the value as int.
   * @param range range the size that percent values are calculared from.
   * @return the result value as int
   */
  public final int getValueAsInt(final float range) {
    return (int) getValue(range);
  }

  /**
   * Get the percent value this value represent.
   * @return the actual percent value.
   */
  private float getPercentValue() {
    if (isPercent()) {
      String percent = value.substring(0, value.length() - PERCENT.length());
      return Float.valueOf(percent);
    } else {
      return 0;
    }
  }

  /**
   * Get the pixel value this value represent.
   * @return the actual pixel value.
   */
  private int getPixelValue() {
    if (isPixel()) {
      String pixel = value.substring(0, value.length() - PIXEL.length());
      return Integer.valueOf(pixel);
    } else {
      return 0;
    }
  }

  /**
   * Checks if this value describes a percent value.
   * @return true if the given string value ends with PERCENT
   * and false otherwise.
   */
  private boolean isPercent() {
    if (value == null) {
      return false;
    } else {
      return value.endsWith(PERCENT);
    }
  }

  /**
   * Checks if this value describes a pixel value.
   * @return true if the given string value ends with PIXEL
   * and false otherwise
   */
  public final boolean isPixel() {
    if (value == null) {
      return false;
    } else {
      return value.endsWith(PIXEL);
    }
  }

  /**
   * toString.
   * @return value
   */
  public String toString() {
    return value;
  }
}
