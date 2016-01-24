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
package de.lessvoid.nifty.types;

import de.lessvoid.niftyinternal.common.ColorStringParser;

import java.util.Random;

/**
 * Immutable Color representation for Nifty. Nifty uses this class when color
 * information is needed or returned by a method. A mutable Color representation
 * is available in a separate class @see NftyMutableColor.
 *
 * @author void
 */
public class NiftyColor {
  private static final NiftyColor Transparent = NiftyColor.fromInt(0x00000000);
  private static final NiftyColor None = NiftyColor.fromInt(0x00000000);
  private static final NiftyColor Black = NiftyColor.fromInt(0x000000FF);
  private static final NiftyColor Gray = NiftyColor.fromInt(0x808080FF);
  private static final NiftyColor White = NiftyColor.fromInt(0xFFFFFFFF);
  private static final NiftyColor Silver = NiftyColor.fromInt(0xC0C0C0FF);
  private static final NiftyColor Maroon = NiftyColor.fromInt(0x800000FF);
  private static final NiftyColor Red = NiftyColor.fromInt(0xFF0000FF);
  private static final NiftyColor Purple = NiftyColor.fromInt(0x800080FF);
  private static final NiftyColor Fuchsia = NiftyColor.fromInt(0xFF00FFFF);
  private static final NiftyColor Green = NiftyColor.fromInt(0x008000FF);
  private static final NiftyColor Lime = NiftyColor.fromInt(0x00FF00FF);
  private static final NiftyColor Olive = NiftyColor.fromInt(0x808000FF);
  private static final NiftyColor Yellow = NiftyColor.fromInt(0xFFFF00FF);
  private static final NiftyColor Navy = NiftyColor.fromInt(0x000080FF);
  private static final NiftyColor Blue = NiftyColor.fromInt(0x0000FFFF);
  private static final NiftyColor Teal = NiftyColor.fromInt(0x008080FF);
  private static final NiftyColor Aqua = NiftyColor.fromInt(0x00FFFFFF);
  /**
   * A helper class to parse color Strings.
   */
  private static final ColorStringParser parser = new ColorStringParser();

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
   * 
   * @param newRed
   *          red component
   * @param newGreen
   *          green component
   * @param newBlue
   *          blue component
   * @param newAlpha
   *          alpha component
   */
  public NiftyColor(final double newRed, final double newGreen, final double newBlue, final double newAlpha) {
    this.red = newRed;
    this.green = newGreen;
    this.blue = newBlue;
    this.alpha = newAlpha;
  }

  /**
   * Copy constructor.
   * 
   * @param color
   *          the other color
   * @return NiftyColor
   */
  public NiftyColor(final NiftyColor color) {
    this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  public static NiftyColor black() {
    return Black;
  }

  public static NiftyColor silver() {
    return Silver;
  }

  public static NiftyColor gray() {
    return Gray;
  }

  public static NiftyColor white() {
    return White;
  }

  public static NiftyColor maroon() {
    return Maroon;
  }

  public static NiftyColor red() {
    return Red;
  }

  public static NiftyColor purple() {
    return Purple;
  }

  public static NiftyColor fuchsia() {
    return Fuchsia;
  }

  public static NiftyColor green() {
    return Green;
  }

  public static NiftyColor lime() {
    return Lime;
  }

  public static NiftyColor olive() {
    return Olive;
  }

  public static NiftyColor yellow() {
    return Yellow;
  }

  public static NiftyColor navy() {
    return Navy;
  }

  public static NiftyColor blue() {
    return Blue;
  }

  public static NiftyColor teal() {
    return Teal;
  }

  public static NiftyColor aqua() {
    return Aqua;
  }

  /**
   * Get a fully transparent black color.
   * 
   * @return NiftyColor
   */
  public static NiftyColor none() {
    return None;
  }

  /**
   * Get a fully transparent color.
   * 
   * @return NiftyColor
   */
  public static NiftyColor transparent() {
    return Transparent;
  }

  /**
   * Returns true if the given String could be parsed as a color.
   * 
   * @return true if the color String can be parsed as a color
   */
  public static boolean isColor(final String color) {
    return parser.isValid(color);
  }

  /**
   * Create a color from a color String formated like in HTML but with alpha,
   * e.g.: "#ff00ffff" or "#f0ff"
   * 
   * @param color
   *          the color string
   */
  public static NiftyColor fromString(final String color) {
    return parser.fromString(color);
  }

  /**
   * Create a color from another color, using the given alpha value.
   * 
   * @param color
   *          color
   * @param alpha
   *          alpha component
   */
  public static NiftyColor fromColorWithAlpha(final NiftyColor color, final double alpha) {
    return new NiftyColor(color.getRed(), color.getGreen(), color.getBlue(), alpha);
  }

  /**
   * Create a color from an encoded int value R + G + B + Alpha.
   * 
   * @param color
   *          color value
   */
  public static NiftyColor fromInt(final int color) {
    int red = (color >> 24) & 0xFF;
    int green = (color >> 16) & 0xFF;
    int blue = (color >> 8) & 0xFF;
    int alpha = (color >> 0) & 0xFF;
    return new NiftyColor(red / 255.f, green / 255.f, blue / 255.f, alpha / 255.f);
  }

  /**
   * Create a color from Hue, Saturation and Value values. With Hue in [0, 360]
   * and Saturation and Value [0, 1].
   * 
   * @return NiftyColor
   */
  public static NiftyColor fromHSV(final double hue, final double saturation, final double value) {
    int h_i = ((int) (hue / 60)) % 6;
    double f = (hue / 60) - (int) (hue / 60);

    double red = 0;
    double green = 0;
    double blue = 0;

    double p = (value * (1.0 - saturation));
    double q = (value * (1.0 - f * saturation));
    double t = (value * (1.0 - saturation * (1.0 - f)));

    switch (h_i) {
    case 0:
      red = value;
      green = t;
      blue = p;
      break;
    case 1:
      red = q;
      green = value;
      blue = p;
      break;
    case 2:
      red = p;
      green = value;
      blue = t;
      break;
    case 3:
      red = p;
      green = q;
      blue = value;
      break;
    case 4:
      red = t;
      green = p;
      blue = value;
      break;
    case 5:
      red = value;
      green = p;
      blue = q;
      break;
    }

    return new NiftyColor(red, green, blue, 1.0f);
  }

  /**
   * Create a random Color with full Alpha (1.0f)
   * 
   * @return a color with random red, green and blue components
   */
  public static NiftyColor randomColor() {
    Random random = new Random();
    return new NiftyColor(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1.f);
  }

  /**
   * Create a random Color with given Alpha
   * 
   * @param alpha
   *          the alpha value to use
   * @return a color with random red, green and blue components
   */
  public static NiftyColor randomColorWithAlpha(final double alpha) {
    Random random = new Random();
    return new NiftyColor(random.nextDouble(), random.nextDouble(), random.nextDouble(), alpha);
  }

  /**
   * Get red component.
   * 
   * @return red component
   */
  public double getRed() {
    return red;
  }

  /**
   * Get green component.
   * 
   * @return green component
   */
  public double getGreen() {
    return green;
  }

  /**
   * Get blue component.
   * 
   * @return blue component
   */
  public double getBlue() {
    return blue;
  }

  /**
   * Get alpha component.
   * 
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
    result.append("#");
    result.append(zeroPadded(Integer.toHexString((int) (red * 255.))));
    result.append(zeroPadded(Integer.toHexString((int) (green * 255.))));
    result.append(zeroPadded(Integer.toHexString((int) (blue * 255.))));
    result.append(zeroPadded(Integer.toHexString((int) (alpha * 255.))));
    result.append(" {");
    result.append(red).append(", ");
    result.append(green).append(", ");
    result.append(blue).append(", ");
    result.append(alpha);
    result.append("}");
    return result.toString();
  }

  public String toHexString() {
    StringBuilder result = new StringBuilder();
    result.append("#");
    result.append(zeroPadded(Integer.toHexString((int) (red * 255.))));
    result.append(zeroPadded(Integer.toHexString((int) (green * 255.))));
    result.append(zeroPadded(Integer.toHexString((int) (blue * 255.))));
    result.append(zeroPadded(Integer.toHexString((int) (alpha * 255.))));
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

  private String zeroPadded(final String hexString) {
    if (hexString.length() == 1) {
      return "0" + hexString;
    }
    return hexString;
  }
}
