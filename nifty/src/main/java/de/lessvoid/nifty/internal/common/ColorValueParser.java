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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.api.NiftyColor;

/**
 * Checks a String if it contains a Nifty inline color. This class is used while
 * parsing text strings for colors.
 * <p/>
 * This has now been changed to not return an Result instance anymore but simply
 * remembers the last result in instance variables. This makes the class not
 * thread-safe but it was not shared between threads anyway.
 *
 * @author void
 */
public class ColorValueParser {
  private ColorValidator colorValidator = new ColorValidator();
  private boolean isColor;
  private int nextIndex;
  @Nullable
  private NiftyColor color;

  public ColorValueParser() {
    setNoResult();
  }

  public boolean isColor() {
    return isColor;
  }

  public int getNextIndex() {
    return nextIndex;
  }

  @Nullable
  public NiftyColor getColor() {
    return color;
  }

  public boolean isColor(@Nonnull final String text, final int startIdx) {
    if (text.startsWith("\\#", startIdx)) {
      int endIdx = text.indexOf('#', startIdx + 2);
      if (endIdx != -1) {
        setResult(text.substring(startIdx + 1, endIdx), endIdx + 1);
        return isColor;
      }
    }
    setNoResult();
    return false;
  }

  private void setNoResult() {
    nextIndex = -1;
    color = null;
    isColor = false;
  }

  private void setResult(@Nonnull final String value, final int endIdx) {
    nextIndex = -1;
    color = null;
    isColor = colorValidator.isValid(value);
    if (isColor) {
      color = NiftyColor.fromString(value);
      nextIndex = endIdx;
    }
  }
}
