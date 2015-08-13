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
package de.lessvoid.niftyinternal;

import javax.annotation.Nonnull;

/**
 * one value:    [applied to all]
 * two values:   [top and bottom], [left and right]
 * three values: [top], [left and right], [bottom]
 * four values:  [top], [right], [bottom], [left]
 *
 * @author void
 */
public class PaddingAttributeParser {
  @Nonnull
  private final String left;
  @Nonnull
  private final String right;
  @Nonnull
  private final String top;
  @Nonnull
  private final String bottom;

  public PaddingAttributeParser(@Nonnull final String input) throws Exception {

    String[] values = input.split(",");
    if (values.length == 0) {
      throw new Exception("parsing error, paddingString is empty");
    }

    int valueCount = values.length;
    if (valueCount == 1) {
      if (values[0].length() == 0) {
        throw new Exception("parsing error, paddingString is empty");
      }
      left = values[0];
      right = values[0];
      top = values[0];
      bottom = values[0];
    } else if (valueCount == 2) {
      left = values[1];
      right = values[1];
      top = values[0];
      bottom = values[0];
    } else if (valueCount == 3) {
      left = values[1];
      right = values[1];
      top = values[0];
      bottom = values[2];
    } else if (valueCount == 4) {
      left = values[3];
      right = values[1];
      top = values[0];
      bottom = values[2];
    } else {
      throw new Exception("parsing error, paddingString count error (" + valueCount + ") expecting value from 1 to 4");
    }
  }

  @Nonnull
  public String getLeft() {
    return left;
  }

  @Nonnull
  public String getTop() {
    return top;
  }

  @Nonnull
  public String getRight() {
    return right;
  }

  @Nonnull
  public String getBottom() {
    return bottom;
  }
}
