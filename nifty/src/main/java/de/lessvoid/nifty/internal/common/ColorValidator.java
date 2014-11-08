/*
 * Copyright (c) 2014, Jens Hohmuth 
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

import java.util.regex.Pattern;

/**
 * This checks a given String that represents a color for being valid. Supported
 * are both short mode "#f12f" and long mode "#ff1122ff"
 * 
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class ColorValidator {
  /**
   * The pattern used to check if the string is valid to be a color definition.
   */
  private static final Pattern colorPattern = Pattern.compile("#\\p{XDigit}{3,8}");

  /**
   * Check if a string fits any type of color definition string.
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a color definition
   */
  boolean isValid(final String toCheck) {
    if (toCheck == null) {
      return false;
    }
    
    final int digits = toCheck.length() - 1;
    
    if (digits == 3 || digits == 4 || digits == 6 || digits == 8) {
      return checkSyntax(toCheck);
    }
    
    return false;
  }

  /**
   * Check if the color text is written in the short form with alpha. The text
   * would have to look like this: <code>#rgba</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a short form color definition
   *         with alpha
   */
  boolean isShortMode(final String toCheck) {
    return isColor(toCheck, 4);
  }

  /**
   * Check if the color text is written in the short form without alpha. The
   * text would have to look like this: <code>#rgb</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a short form color definition
   *         without alpha
   */
  boolean isShortModeWithoutAlpha(final String toCheck) {
    return isColor(toCheck, 3);
  }

  /**
   * Check if the color text is written in the long form with alpha. The text
   * would have to look like this: <code>#rrggbbaa</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a long form color definition
   *         with alpha
   */
  boolean isLongMode(final String toCheck) {
    return isColor(toCheck, 8);
  }

  /**
   * Check if the color text is written in the long form without alpha. The text
   * would have to look like this: <code>#rrggbb</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a long form color definition
   *         without alpha
   */
  boolean isLongModeWithoutAlpha(final String toCheck) {
    return isColor(toCheck, 6);
  }

  /**
   * Test of a text contains a color definition with a specified amount of color
   * digits.
   * 
   * @param toCheck
   *          the text to check
   * @param components
   *          the amount of digits to be used to define the color
   * @return <code>true</code> if the text fits a color text and meets the
   *         required length exactly
   */
  private boolean isColor(final String toCheck, final int components) {
    return (toCheck != null && toCheck.length() == (components + 1) && checkSyntax(toCheck));
  }

  /**
   * Check if the general syntax of the color string fits. That test does not
   * validate that the string has the required length.
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text matches the required syntax
   */
  private boolean checkSyntax(final String toCheck) {
    return colorPattern.matcher(toCheck).matches();
  }
}
