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
public final class SizeValue {
  /**
   * Max percent constant.
   */
  public static final float MAX_PERCENT = 100.0f;

  /**
   * The shared instance of the default size value.
   */
  @Nonnull
  private static final SizeValue DEF = new SizeValue(SizeValueType.Default);

  /**
   * The shared instance of a maximum size value.
   */
  @Nonnull
  private static final SizeValue MAX = new SizeValue(SizeValueType.Maximum);

  /**
   * The shared instance of the 0px size value. This is used to extremely often that it comes in handy to share the
   * instance for some minor optimization.
   */
  @Nonnull
  private static final SizeValue NULL_PX = new SizeValue(0, SizeValueType.Pixel);

  /**
   * The shared instance of a sum size value.
   */
  @Nonnull
  private static final SizeValue SUM = new SizeValue(SizeValueType.Sum);

  /**
   * The shared instance of a wildcard size value.
   */
  @Nonnull
  private static final SizeValue WILDCARD = new SizeValue(SizeValueType.Wildcard);

  /**
   * The type of this size value.
   */
  @Nonnull
  private final SizeValueType type;

  /**
   * The value stored in this size value, check {@link #hasCalculatedValue} and {@link #hasValue} to ensure that this
   * value is valid.
   */
  private final float value;

  /**
   * This is set {@code true} in case the {@link #value} is set as calculated value.
   */
  private final boolean hasCalculatedValue;

  /**
   * This is set {@code true} in case the {@link #value} is set as normal value.
   */
  private final boolean hasValue;

  /**
   * Create a new instance of the size value that only contains a type. This only works for types that allow to be
   * used without value.
   *
   * @param type the type
   * @throws java.lang.IllegalArgumentException in case the type requires a value
   * @see SizeValueType#getValueRequirement()
   */
  public SizeValue(@Nonnull final SizeValueType type) {
    if (type.getValueRequirement() == SizeValueType.ValueRequirement.Required) {
      throw new IllegalArgumentException("Size value type " + type.name() + " requires a value!");
    }
    this.type = type;
    value = 0.0f;
    hasValue = false;
    hasCalculatedValue = false;
  }

  /**
   * Create a new instance of the size value that contains a value and a type. This only works for types that allow a
   * fixed set value.
   *
   * @param value the value
   * @param type  the type
   * @throws java.lang.IllegalArgumentException in case the type forbids the usage of a value or only accepts a
   *                                            calculated value
   * @see SizeValueType#getValueRequirement()
   */
  public SizeValue(final int value, @Nonnull final SizeValueType type) {
    switch (type.getValueRequirement()) {
      case Forbidden:
        throw new IllegalArgumentException("Size value type " + type.name() + " does not allow a value.");
      case CalculatedOnly:
        throw new IllegalArgumentException("Size value type " + type.name() + " does only allow calculated values");
    }
    this.type = type;
    this.value = value;
    hasValue = true;
    hasCalculatedValue = false;
  }

  /**
   * Create a new instance of a size value that contains a computed value and a type. This only works for values that
   * do not require a fixed value and allow a computed value.
   *
   * @param type            the type
   * @param calculatedValue the computed value
   * @throws java.lang.IllegalArgumentException in case the type forbids the usage of values or requires a fixed set
   *                                            value    *
   * @see SizeValueType#getValueRequirement()
   */
  public SizeValue(@Nonnull final SizeValueType type, final int calculatedValue) {
    switch (type.getValueRequirement()) {
      case Forbidden:
        throw new IllegalArgumentException("Size value type " + type.name() + " does not allow a value.");
      case Required:
        throw new IllegalArgumentException("Size value type " + type.name() + " requires a value!");
    }
    this.type = type;
    this.value = calculatedValue;
    hasValue = false;
    hasCalculatedValue = true;
  }

  /**
   * This constructor is used to parse a size value from a string.
   * <p />
   * This is the most expensive way to create a size value. Only use this if you really need to parse a string to get
   * the size value. This method does <b>not</b> allow to set computed values as those are only set by the layout
   * process that does not use this method to create its instances.
   *
   * @param valueParam the size value as string
   * @throws java.lang.IllegalArgumentException in case its not possible to parse the value
   */
  public SizeValue(@Nullable final String valueParam) {
    hasCalculatedValue = false;
    if (valueParam == null || valueParam.isEmpty() || valueParam.equals("default")) { // alias for "d"
      type = SizeValueType.Default;
      value = 0.0f;
      hasValue = false;
      return;
    } else if (valueParam.equals("sum")) { // alias for "s"
      type = SizeValueType.Sum;
      value = 0.0f;
      hasValue = false;
      return;
    } else if (valueParam.equals("max")) { // alias for "m"
      type = SizeValueType.Maximum;
      value = 0.0f;
      hasValue = false;
      return;
    }

    SizeValueType selectedType = null;
    for (@Nonnull final SizeValueType currentType : SizeValueType.values()) {
      if (valueParam.endsWith(currentType.getExtension())) {
        selectedType = currentType;
        break;
      }
    }

    if (selectedType == null) {
      // no suffix -> falling back to px
      type = SizeValueType.Pixel;
      try {
        value = Float.valueOf(valueParam);
        hasValue = true;
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("String value [" + valueParam + "] does not fit the required format.", e);
      }
    } else {
      type = selectedType;

      final int paramLength = valueParam.length();
      final int extensionLength = type.getExtension().length();

      if (paramLength > extensionLength) {
        // seems like we got a value
        switch (type.getValueRequirement()) {
          case CalculatedOnly:
            throw new IllegalArgumentException("Setting the value by string is not allowed for types that only allow " +
                "a calculated value.");
          case Forbidden:
            throw new IllegalArgumentException("The size type " + type.name() + " does not allow any values.");
        }
        try {
          value = Float.valueOf(valueParam.substring(0, paramLength - extensionLength));
          hasValue = true;
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("String value [" + valueParam + "] does not fit the required format.", e);
        }
      } else {
        if (type.getValueRequirement() == SizeValueType.ValueRequirement.Required) {
          throw new IllegalArgumentException("Size value type " + type.name() + " requires a value!");
        }
        value = 0.0f;
        hasValue = false;
      }
    }
  }

  /**
   * Get the default size value.
   *
   * @return a instance of the default size value
   * @see de.lessvoid.nifty.tools.SizeValueType#Default
   */
  @Nonnull
  public static SizeValue def() {
    return DEF;
  }

  /**
   * Get a instance of the default size value with a attached calculated size value.
   *
   * @param pixelValue the calculated pixel size
   * @return the default size value instance with the attached calculated size
   * @see de.lessvoid.nifty.tools.SizeValueType#Default
   */
  @Nonnull
  public static SizeValue def(final int pixelValue) {
    return new SizeValue(SizeValueType.Default, pixelValue);
  }

  /**
   * Create a pixel based size value.
   *
   * @param pixelValue pixel value
   * @return the size value instance representing the pixel value
   * @see de.lessvoid.nifty.tools.SizeValueType#Pixel
   */
  @Nonnull
  public static SizeValue px(final int pixelValue) {
    if (pixelValue == 0) {
      return NULL_PX;
    }
    return new SizeValue(pixelValue, SizeValueType.Pixel);
  }

  /**
   * Create a percentage based size value.
   *
   * @param percentage percentage value
   * @return the size value instance representing the percentage value
   * @see de.lessvoid.nifty.tools.SizeValueType#Percent
   */
  @Nonnull
  public static SizeValue percent(final int percentage) {
    return new SizeValue(percentage, SizeValueType.Percent);
  }

  /**
   * Create a percentage based size value. This percentage will use the height of the parent element as reference.
   * This value is only allowed to be used as width value.
   *
   * @param percentage percentage value
   * @return the size value instance representing the percentage value
   * @see de.lessvoid.nifty.tools.SizeValueType#PercentHeight
   */
  @Nonnull
  public static SizeValue percentHeight(final int percentage) {
    return new SizeValue(percentage, SizeValueType.PercentHeight);
  }

  /**
   * Create a percentage based size value. This percentage will use the width of the parent element as reference.
   * This value is only allowed to be used as height value.
   *
   * @param percentage percentage value
   * @return the size value instance representing the percentage value
   * @see de.lessvoid.nifty.tools.SizeValueType#PercentWidth
   */
  @Nonnull
  public static SizeValue percentWidth(final int percentage) {
    return new SizeValue(percentage, SizeValueType.PercentWidth);
  }

  /**
   * Get a wildcard size value.
   *
   * @return the size value instance representing the wildcard value
   * @see de.lessvoid.nifty.tools.SizeValueType#Wildcard
   */
  @Nonnull
  public static SizeValue wildcard() {
    return WILDCARD;
  }

  /**
   * Create a wildcard size value that stores a computed value.
   *
   * @param computedValue the computed size
   * @return the size value instance representing the wildcard with computed size
   * @see de.lessvoid.nifty.tools.SizeValueType#Wildcard
   */
  @Nonnull
  public static SizeValue wildcard(final int computedValue) {
    return new SizeValue(SizeValueType.Wildcard, computedValue);
  }

  /**
   * Get a sum size value.
   *
   * @return the size value instance representing the sum value
   * @see de.lessvoid.nifty.tools.SizeValueType#Sum
   */
  @Nonnull
  public static SizeValue sum() {
    return SUM;
  }

  /**
   * Create a sum size value that stores a computed value.
   *
   * @param computedValue the computed size
   * @return the size value instance representing the sum with computed size
   * @see de.lessvoid.nifty.tools.SizeValueType#Sum
   */
  @Nonnull
  public static SizeValue sum(final int computedValue) {
    return new SizeValue(SizeValueType.Sum, computedValue);
  }

  /**
   * Get a maximum size value.
   *
   * @return the size value instance representing the maximum value
   * @see de.lessvoid.nifty.tools.SizeValueType#Maximum
   */
  @Nonnull
  public static SizeValue max() {
    return MAX;
  }

  /**
   * Create a maximum size value that stores a computed value.
   *
   * @param computedValue the computed size
   * @return the size value instance representing the maximum with computed size
   * @see de.lessvoid.nifty.tools.SizeValueType#Maximum
   */
  @Nonnull
  public static SizeValue max(final int computedValue) {
    return new SizeValue(SizeValueType.Maximum, computedValue);
  }

  /**
   * Do we need to know the size of the parent element to calculate this value?
   *
   * @return {@code true} if the size of this value can be calculated without knowing about the parent.
   */
  public boolean isIndependentFromParent() {
    return type.isIndependent();
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
    return hasValue || hasCalculatedValue;
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
      return (range / MAX_PERCENT) * value;
    } else if (isPixel()) {
      return value;
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
    if (isPercent()) {
      return Math.round((range / MAX_PERCENT) * value);
    } else if (isPixel()) {
      return Math.round (value);
    } else {
      return -1;
    }
  }

  /**
   * Get a string representation of this SizeValue that complies with the formatting
   * standards for {@link #SizeValue(String)} constructor.
   * @return a well-formed string representation of this SizeValue
   */
  public String getValueAsString()
  {
    final StringBuilder builder = new StringBuilder();
    if (hasValue) {
      builder.append(value);
    }
    builder.append(type.getExtension());
    return builder.toString();
  }

  /**
   * Checks if this value describes a pixel value.
   *
   * @return true if the given string value ends with PIXEL
   * and false otherwise
   */
  public boolean isPixel() {
    return hasValue() && !isPercent();
  }

  /**
   * Checks if this value describes a percent value.
   *
   * @return true if the given string value ends with PERCENT
   * and false otherwise.
   */
  public boolean isPercent() {
    if (!hasValue()) {
      return false;
    }
    switch (type) {
      case Percent:
      case PercentWidth:
      case PercentHeight:
        return true;
      default:
        return false;
    }
  }

  /**
   * Get a generic string representation of this size value.
   * <p />
   * The output of this function is most likely <b>not</b> valid to be parsed by the {@link #SizeValue(String)}
   * constructor as it contains the current calculated value and this constructor does not support this kind of value.
   * To get a String value parseable by the {@link #SizeValue(String)} constructor, use {@link #getValueAsString()}.
   *
   * @return the string representation of this instance
   */
  @Nonnull
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (hasValue) {
      builder.append(value);
    }
    builder.append(type.getExtension());
    if (hasCalculatedValue) {
      builder.append('[').append(value).append("px]");
    }
    return builder.toString();
  }

  public boolean hasDefault() {
    return type == SizeValueType.Default;
  }

  public boolean hasWidthSuffix() {
    return type == SizeValueType.PercentWidth;
  }

  public boolean hasHeightSuffix() {
    return type == SizeValueType.PercentHeight;
  }

  public boolean hasWildcard() {
    return type == SizeValueType.Wildcard;
  }

  public boolean hasSum() {
    return type == SizeValueType.Sum;
  }

  public boolean hasMax() {
    return type == SizeValueType.Maximum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SizeValue sizeValue = (SizeValue) o;

    if (hasCalculatedValue != sizeValue.hasCalculatedValue) return false;
    if (hasValue != sizeValue.hasValue) return false;
    if (Float.compare(sizeValue.value, value) != 0) return false;
    if (type != sizeValue.type) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
    result = 31 * result + (hasCalculatedValue ? 1 : 0);
    result = 31 * result + (hasValue ? 1 : 0);
    return result;
  }
}
