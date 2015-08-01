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
package de.lessvoid.nifty.internal.common;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.types.NiftyColor;

/**
 * Helper class to create Color instances from String representation.
 * @author void
 */
public class ColorStringParser {
  private static Logger log = Logger.getLogger(ColorStringParser.class.getName());

  /**
   * ColorValidator.
   */
  private static ColorValidator colorValidator = new ColorValidator();

  /**
   * scale short mode factor (converts 0x5 to 0x55).
   */
  private static final int SCALE_SHORT_MODE = 0x11;

  /**
   * max value for conversion.
   */
  private static final float MAX_INT_VALUE = 255.0f;

  /**
   * hex base to convert numbers.
   */
  private static final int HEX_BASE = 16;

  /*
   * CSS3 color constants we support. 
   */
  private static final Map<String, String> COLOR_MAP = new HashMap<String, String>();
  {
    COLOR_MAP.put("black",   "#000000");
    COLOR_MAP.put("silver",  "#C0C0C0");
    COLOR_MAP.put("gray",    "#808080");
    COLOR_MAP.put("white",   "#FFFFFF");
    COLOR_MAP.put("maroon",  "#800000");
    COLOR_MAP.put("red",     "#FF0000");
    COLOR_MAP.put("purple",  "#800080");
    COLOR_MAP.put("fuchsia", "#FF00FF");
    COLOR_MAP.put("green",   "#008000");
    COLOR_MAP.put("lime",    "#00FF00");
    COLOR_MAP.put("olive",   "#808000");
    COLOR_MAP.put("yellow",  "#FFFF00");
    COLOR_MAP.put("navy",    "#000080");
    COLOR_MAP.put("blue",    "#0000FF");
    COLOR_MAP.put("teal",    "#008080");
    COLOR_MAP.put("aqua",    "#00FFFF");
  } 

  /**
   * Checks if the given color String can be parsed.
   * @return true if color is a valid color String and false if not.
   */
  public boolean isValid(final String color) {
    if (colorValidator.isShortModeWithoutAlpha(color)) {
      return true;
    } else if (colorValidator.isLongModeWithoutAlpha(color)) {
      return true;
    } else if (colorValidator.isValid(color)) {
      return true;
    } else if (isColorConstant(color)) {
      return true;
    }
    return false;
  }

  /**
   * Create a new Color instance from a color String.
   * @param color color String in short mode without alpha ("f09") or in short mode with alpha ("f09a") or in long
   * mode without alpha ("ff0099") or with alpha ("ff0099aa") 
   * @return Color instance
   */
  public NiftyColor fromString(final String color) {
    float red = 1.f;
    float green = 1.f;
    float blue = 1.f;
    float alpha = 1.f;

    if (colorValidator.isShortModeWithoutAlpha(color)) {
      red = getRFromString(color);
      green = getGFromString(color);
      blue = getBFromString(color);
      if (log.isLoggable(Level.FINE)) {
        log.fine("found short mode color [" + color + "] with missing alpha value automatically adjusted with alpha value of [#f]");
      }
    } else if (colorValidator.isLongModeWithoutAlpha(color)) {
      red = getRFromString(color);
      green = getGFromString(color);
      blue = getBFromString(color);
      if (log.isLoggable(Level.FINE)) {
        log.fine("found long mode color [" + color + "] with missing alpha value automatically adjusted with alpha value of [#ff]");
      }
    } else if (colorValidator.isValid(color)) {
      red = getRFromString(color);
      green = getGFromString(color);
      blue = getBFromString(color);
      alpha = getAFromString(color);
    } else if (isColorConstant(color)) {
      String colorString = COLOR_MAP.get(color.toLowerCase());
      red = getRFromString(colorString);
      green = getGFromString(colorString);
      blue = getBFromString(colorString);
    } else {
      log.fine("error parsing color [" + color + "] automatically adjusted to white [#ffffffff]");
      red = green = blue = alpha = 1.0f;
    }
    return new NiftyColor(red, green, blue, alpha);
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
    return colorValidator.isShortMode(color) || colorValidator.isShortModeWithoutAlpha(color);
  }

  /**
   * Returns true if the color is part of the constant color map.
   * @param color the color string
   * @return true when this is a constant color
   */
  private boolean isColorConstant(final String color) {
    return COLOR_MAP.containsKey(color.toLowerCase());
  }
}
