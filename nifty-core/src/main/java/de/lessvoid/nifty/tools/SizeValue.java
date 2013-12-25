package de.lessvoid.nifty.tools;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * The SizeValue class stores and manages size value strings. Such strings are used to store size representations.
 * See the constants for all supported special kind of values.
 *
 * @author void &lt;void@lessvoid.com&gt;
 * @author Joris van der Wel &lt;joris@jorisvanderwel.com&gt;
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public class SizeValue {
  /**
   * The default value. This is used if the attribute is unset or null. It can also be used to track the pixel value
   * that the default resolves to.
   */
  @Nonnull
  public static final String DEFAULT = "d";

  /**
   * Add a PIXEL to some size value to indicate a pixel value.
   * <p/>
   * Example: "100px" or "640px"
   */
  @Nonnull
  public static final String PIXEL = "px";

  /**
   * Add a PERCENT to some size value to indicate a percent value.
   * <p/>
   * Example: "100%" or "50%"
   */
  @Nonnull
  public static final String PERCENT = "%";

  /**
   * Add a WIDTH_SUFFIX to some size value to indicate that this value will be calculated in respect to the Width of
   * an element. This is only appropriate to a height attribute and this class can only detect it's present. Handling
   * must be performed outside of this class.
   */
  @Nonnull
  public static final String WIDTH_SUFFIX = "w";

  /**
   * Add a HEIGHT_SUFFIX to some size value to indicate that this value will be calculated in respect to the Height
   * of an element. This is only appropriate to a width attribute and this class can only detect it's present.
   * Handling must be performed outside of this class.
   */
  @Nonnull
  public static final String HEIGHT_SUFFIX = "h";

  /**
   * The WILDCARD value will not really be handled by the SizeValue class. Its used to use the maximum available
   * space by some layout managers.
   */
  @Nonnull
  public static final String WILDCARD = "*";

  /**
   * The SUM_SUFFIX value will not really be handled by the SizeValue class.
   * <p/>
   * When set, this size value will be the sum of the sizes of the content/children  of the element it is used on.
   * Only children with absolute size values will be considered, or children that also have a sum/max size value.
   * <p/>
   * Builders or XML should use the value "s" or "sum". A pixel value can also be given in the form of "100s",
   * but this is for internal use (it represents the calculated width).
   */
  @Nonnull
  public static final String SUM = "s";

  /**
   * The MAX_SUFFIX value will not really be handled by the SizeValue class.
   * <p/>
   * When set, this size value will be the highest of the sizes of the content/children  of the element it is used on.
   * Only children with absolute size values will be considered, or children that also have a sum/max size value.
   * <p/>
   * Builders or XML should use the value "m" or "max". A pixel value can also be given in the form of "100m",
   * but this is for internal use (it represents the calculated width).
   */
  @Nonnull
  public static final String MAX = "m";

  /**
   * Max percent constant.
   */
  public static final float MAX_PERCENT = 100.0f;

  /**
   * The current value that has been set.
   */
  @Nonnull
  private final String value;

  /**
   * percent value.
   */
  private final float percentValue;

  /**
   * pixel value.
   */
  private final float pixelValue;

  private boolean isIndependent;
  private boolean hasValue;
  private boolean isPixel;
  private boolean isPercent;

  /**
   * Matches DEFAULT.
   */
  private boolean hasDefault;

  /**
   * Matches WILDCARD.
   */
  private boolean hasWildcard;

  /**
   * value has SUM_SUFFIX attached.
   */
  private boolean hasSum;

  /**
   * value has MAX_SUFFIX attached.
   */
  private boolean hasMax;

  /**
   * value has WIDTH_SUFFIX attached.
   */
  private boolean hasWidthSuffix;

  /**
   * value has HEIGHT_SUFFIX attached.
   */
  private boolean hasHeightSuffix;

  /**
   * Create a new instance using the given value.
   *
   * @param valueParam the String value
   */
  public SizeValue(@Nullable String valueParam) {
    isIndependent = true;
    hasValue = false;
    isPixel = false;
    isPercent = false;

    if (valueParam == null || valueParam.length() == 0 || valueParam.equals("default")) { // alias for "d"
      valueParam = DEFAULT;
      hasDefault = true;
      isPixel = true;
    } else if (valueParam.equals("sum")) { // alias for "s"
      valueParam = SUM;
      hasSum = true;
      isPixel = true;
    } else if (valueParam.equals("max")) { // alias for "m"
      valueParam = MAX;
      hasMax = true;
      isPixel = true;
    } else if (valueParam.endsWith(WILDCARD)) {
      hasWildcard = true;
      isIndependent = false;
    } else if (valueParam.endsWith(DEFAULT)) {
      hasDefault = true;
      isPixel = true;
      hasValue = valueParam.length() > DEFAULT.length();
    } else if (valueParam.endsWith(SUM)) {
      hasSum = true;
      isPixel = true;
      hasValue = valueParam.length() > SUM.length();
    } else if (valueParam.endsWith(MAX)) {
      hasMax = true;
      isPixel = true;
      hasValue = valueParam.length() > MAX.length();
    } else if (valueParam.endsWith(PERCENT + WIDTH_SUFFIX)) {
      hasWidthSuffix = true;
      isIndependent = false;
      isPercent = true;
      hasValue = valueParam.length() > PERCENT.length() + WIDTH_SUFFIX.length();
    } else if (valueParam.endsWith(PERCENT + HEIGHT_SUFFIX)) {
      hasHeightSuffix = true;
      isIndependent = false;
      isPercent = true;
      hasValue = valueParam.length() > PERCENT.length() + HEIGHT_SUFFIX.length();
    } else if (valueParam.endsWith(PERCENT)) {
      isIndependent = false;
      isPercent = true;
      hasValue = valueParam.length() > PERCENT.length();
    } else if (valueParam.endsWith(PIXEL)) {
      isPixel = true;
      hasValue = valueParam.length() > PERCENT.length();
    } else { // "123"
      isPixel = true;
      hasValue = true;
    }

    this.value = valueParam;

    this.percentValue = getPercentValue();
    this.pixelValue = getPixelValue();


  }

  @Nonnull
  private static final SizeValue DEF = new SizeValue(null);

  @Nonnull
  public static SizeValue def() {
    return DEF;
  }

  @Nonnull
  public static SizeValue def(int pixelValue) {
    return new SizeValue(pixelValue + DEFAULT);
  }

  private static final SizeValue NULL_PX = new SizeValue("0" + PIXEL);

  /**
   * static helper to create a pixel based SizeValue.
   *
   * @param pixelValue pixel value
   * @return SizeValue
   */
  @Nonnull
  public static SizeValue px(final int pixelValue) {
    if (pixelValue == 0) {
      return NULL_PX;
    }
    return new SizeValue(pixelValue + PIXEL);
  }

  /**
   * static helper to create a percentage based SizeValue.
   *
   * @param percentage percentage value
   * @return SizeValue
   */
  @Nonnull
  public static SizeValue percent(final int percentage) {
    return new SizeValue(percentage + PERCENT);
  }

  private static final SizeValue WC = new SizeValue(WILDCARD);

  /**
   * static helper to create a wildcard based SizeValue.
   *
   * @return SizeValue
   */
  @Nonnull
  public static SizeValue wildcard() {
    return WC;
  }

  @Nonnull
  public static SizeValue wildcard(int computedValue) {
    return new SizeValue(computedValue + WILDCARD);
  }

  @Nonnull
  public static SizeValue sum() {
    return new SizeValue(SUM);
  }

  @Nonnull
  public static SizeValue sum(int computedValue) {
    return new SizeValue(computedValue + SUM);
  }

  @Nonnull
  public static SizeValue max() {
    return new SizeValue(MAX);
  }

  @Nonnull
  public static SizeValue max(int computedValue) {
    return new SizeValue(computedValue + MAX);
  }

  /**
   * Do we need to know the size of the parent element to calculate this value?
   *
   * @return {@code true} if the size of this value can be calculated without knowing about the parent.
   */
  public boolean isIndependentFromParent() {
    return isIndependent;
  }

  /**
   * Check if the size value contains a value either in percent or pixel.
   *
   * @return {@code true} in case its a pixel or percent value
   * @deprecated function got renamed to {@link #hasValue()}
   */
  @Deprecated
  public boolean isPercentOrPixel() {
    return hasValue();
  }

  /**
   * Checks if the value contains either PERCENT or PIXEL.
   *
   * @return true when either PERCENT or PIXEL is given.
   */
  public boolean hasValue() {
    return isPercent() || isPixel();
  }

  /**
   * Get the value as a float. WARNING: DO NOT CAST THE RETURN VALUE TO AN INTEGER - use {@link #getValueAsInt(float)}
   * or you will have off-by-one errors!
   *
   * @param range the size that percent values are calculated from.
   * @return the resulting value as a float.
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
   *
   * @param range the size that percent values are calculated from.
   * @return the resulting value rounded to the nearest integer.
   */
  public int getValueAsInt(final float range) {
    return Math.round(getValue(range));
  }

  /**
   * Checks if this value describes a pixel value.
   *
   * @return true if the given string value ends with PIXEL
   * and false otherwise
   */
  public boolean isPixel() {
    return isPixel && hasValue;
  }

  /**
   * Checks if this value describes a percent value.
   *
   * @return true if the given string value ends with PERCENT
   * and false otherwise.
   */
  public boolean isPercent() {
    return isPercent;
  }

  /**
   * toString.
   *
   * @return value
   */
  @Nonnull
  @Override
  public String toString() {
    return value;
  }

  public boolean hasDefault() {
    return hasDefault;
  }

  public boolean hasWidthSuffix() {
    return hasWidthSuffix;
  }

  public boolean hasHeightSuffix() {
    return hasHeightSuffix;
  }

  public boolean hasWildcard() {
    return hasWildcard;
  }

  public boolean hasSum() {
    return hasSum;
  }

  public boolean hasMax() {
    return hasMax;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (super.equals(obj)) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SizeValue)) {
      return false;
    }
    SizeValue other = (SizeValue) obj;
    return value.equals(other.value);
  }

  @Nonnull
  private String getValueWithoutSuffix() {
    for (int i = value.length() - 1; i >= 0; --i) {
      if (value.charAt(i) >= '0' && value.charAt(i) <= '9') {
        return value.substring(0, i + 1);
      }
    }
    return "0";
  }

  /**
   * Get the percent value this value represent.
   *
   * @return the actual percent value.
   */
  private float getPercentValue() {
    if (isPercent()) {
      return Float.valueOf(getValueWithoutSuffix());
    } else {
      return 0;
    }
  }

  /**
   * Get the pixel value this value represent.
   *
   * @return the actual pixel value.
   */
  private int getPixelValue() {
    if (isPixel()) {
      return Integer.valueOf(getValueWithoutSuffix());
    }
    return 0;
  }
}
