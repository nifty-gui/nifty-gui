package de.lessvoid.nifty.layout;


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
  public static final String PIXEL = "px";

  /**
   * Add a PERCENT to some size value to indicate a percent value.
   * Example: "100%" or "50%"
   */
  public static final String PERCENT = "%";

  /**
   * Add a WIDTH_SUFFIX to some size value to indicate that this value
   * will be calculated in respect to the Width of an element. This
   * is only appropriate to a height attribute and this class can only
   * detect it's present. Handling must be performed outside of this class.
   */
  public static final String WIDTH_SUFFIX = "w";

  /**
   * Add a HEIGHT_SUFFIX to some size value to indicate that this value
   * will be calculated in respect to the Height of an element. This
   * is only appropriate to a width attribute and this class can only
   * detect it's present. Handling must be performed outside of this class.
   */
  public static final String HEIGHT_SUFFIX = "h";

  /**
   * The WILDCARD value will not really be handled by the SizeValue class.
   * Its used to use the maximum available space by some layout managers.
   */
  public static final String WILDCARD = "*";

  /**
   * Max percent constant.
   */
  public static final float MAX_PERCENT = 100.0f;

  /**
   * The current value that has been set.
   */
  private final String value;

  /**
   * percent value.
   */
  private final float percentValue;

  /**
   * pixel value.
   */
  private final float pixelValue;

  /**
   * value has WIDTH_SUFFIX attached.
   */
  private final boolean hasWidthSuffix;

  /**
   * value has HEIGHT_SUFFIX attached.
   */
  private final boolean hasHeightSuffix;

  /**
   * Create a new instance using the given value. See the new factory methods px() and percent() as well!
   * @param valueParam the String value
   */
  public SizeValue(final String valueParam) {
    if (valueParam != null) {
      if (valueParam.endsWith(PERCENT + WIDTH_SUFFIX)) {
        this.hasWidthSuffix = true;
        this.hasHeightSuffix = false;
        this.value = valueParam.substring(0, valueParam.length() - 1);
      } else if (valueParam.endsWith(PERCENT + HEIGHT_SUFFIX)) {
        this.hasWidthSuffix = false;
        this.hasHeightSuffix = true;
        this.value = valueParam.substring(0, valueParam.length() - 1);
      } else {
        this.hasWidthSuffix = false;
        this.hasHeightSuffix = false;
        this.value = valueParam;
      }
    } else {
      this.hasWidthSuffix = false;
      this.hasHeightSuffix = false;
      this.value = valueParam;
    }
    this.percentValue = getPercentValue();
    this.pixelValue = getPixelValue();
  }

  /**
   * static helper to create a pixel based SizeValue.
   * @param pixelValue pixel value
   * @return SizeValue
   */
  public static SizeValue px(final int pixelValue) {
    return new SizeValue(pixelValue + PIXEL);
  }

  /**
   * static helper to create a percentage based SizeValue.
   * @param percentage percentage value
   * @return SizeValue
   */
  public static SizeValue percent(final int percentage) {
    return new SizeValue(percentage + PERCENT);
  }

  /**
   * static helper to create a wildcard based SizeValue.
   * @return SizeValue
   */
  public static SizeValue wildcard() {
    return new SizeValue(WILDCARD);
  }

  /**
   * Checks if the value contains either PERCENT or PIXEL.
   * @return true when either PERCENT or PIXEL is given.
   */
  public boolean isPercentOrPixel() {
    return isPercent() || isPixel();
  }

  /**
   * Get the value as float.
   * @param range the size that percent values are calculated from.
   * @return the result value as float
   */
  public float getValue(final float range) {
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
   * @param range range the size that percent values are calculated from.
   * @return the result value as int
   */
  public int getValueAsInt(final float range) {
    return (int) getValue(range);
  }

  /**
   * Checks if this value describes a pixel value.
   * @return true if the given string value ends with PIXEL
   * and false otherwise
   */
  public boolean isPixel() {
    if (value == null) {
      return false;
    } else {
      return !value.equals(WILDCARD) && (value.endsWith(PIXEL) || hasNoSuffix());
    }
  }

  /**
   * toString.
   * @return value
   */
  public String toString() {
    return value;
  }

  public boolean hasWidthSuffix() {
    return hasWidthSuffix;
  }

  public boolean hasHeightSuffix() {
    return hasHeightSuffix;
  }

  public boolean hasWildcard() {
    return "*".equals(value);
  }

  public boolean equals(final Object obj) {
    if (obj instanceof SizeValue) {
      return this.value.equals(((SizeValue) obj).value);
    }
    return false;
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
      if (hasNoSuffix()) {
        return Integer.valueOf(value);
      }
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

  private boolean hasNoSuffix() {
    if (value == null) {
      return false;
    }

    if (value.endsWith(PIXEL) ||
        value.endsWith(PERCENT) ||
        value.endsWith(WIDTH_SUFFIX) ||
        value.endsWith(HEIGHT_SUFFIX)) {
      return false;
    }
    return true;
  }
}
