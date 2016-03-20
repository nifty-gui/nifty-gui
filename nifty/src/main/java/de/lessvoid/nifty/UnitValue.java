/*
 * Copyright (c) 2015, Nifty GUI Community 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty;


/**
 * The UnitValue class stores and manages a value with an optional unit. Such strings are used to store values that
 * represents pixel, percent or special values. See the public constants in this class for all supported special
 * values.
 *
 * This class is immutable.
 *
 * @author void
 */
public class UnitValue {

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
  public UnitValue(final String valueParam) {
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
  public static UnitValue px(final int pixelValue) {
    return new UnitValue(pixelValue + PIXEL);
  }

  /**
   * static helper to create a percentage based SizeValue.
   * @param percentage percentage value
   * @return SizeValue
   */
  public static UnitValue percent(final int percentage) {
    return new UnitValue(percentage + PERCENT);
  }

  /**
   * static helper to create a wildcard based SizeValue.
   * @return SizeValue
   */
  public static UnitValue wildcard() {
    return new UnitValue(WILDCARD);
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
    if (obj instanceof UnitValue) {
      return this.value.equals(((UnitValue) obj).value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = this.value != null ? this.value.hashCode() : 0;
    result = 31 * result + (this.percentValue != +0.0f ? Float.floatToIntBits(this.percentValue) : 0);
    result = 31 * result + (this.pixelValue != +0.0f ? Float.floatToIntBits(this.pixelValue) : 0);
    result = 31 * result + (this.hasWidthSuffix ? 1 : 0);
    result = 31 * result + (this.hasHeightSuffix ? 1 : 0);
    return result;
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
