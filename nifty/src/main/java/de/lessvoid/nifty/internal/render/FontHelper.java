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
package de.lessvoid.nifty.internal.render;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.NiftyFont;

public class FontHelper {

  /**
   * Get character index into the given text that is no more than the given width pixels away.
   *
   * @param font font
   * @param text the string to check
   * @param width the width to check
   * @return the character index into the string that completely fits into the given width.
   */
  public int getVisibleCharactersFromStart(
      @Nonnull final NiftyFont font,
      @Nonnull final CharSequence text,
      final int width,
      final float size) {
    int widthRemaining = width;

    for (int i = 0; i < text.length(); i++) {
      char currentCharacter = text.charAt(i);
      char nextCharacter = getNextCharacter(text, i);

      int w = font.getCharacterWidth(currentCharacter, nextCharacter, size);
      if (w != -1) {
        widthRemaining -= w;
        if (widthRemaining < 0) {
          // this character will underflow the width. we return the last save index.
          return i;
        }
      }
    }
    return text.length();
  }

  /**
   * Get character index from the end of the given text that fits into the given width.
   *
   * @param font font
   * @param text the string to check
   * @param width the minimum width from the end of the string to fit the characters
   * @return the character index into the string.
   */
  public int getVisibleCharactersFromEnd(
      @Nonnull final NiftyFont font,
      @Nonnull final CharSequence text,
      final int width,
      final float size) {
    int widthRemaining = width;

    for (int i = text.length() - 1; i >= 0; i--) {
      char currentCharacter = text.charAt(i);
      char prevCharacter = getPrevCharacter(text, i);

      int w = font.getCharacterWidth(prevCharacter, currentCharacter, size);
      if (w != -1) {
        widthRemaining -= w;
        if (widthRemaining < 0) {
          // this character will underflow the width. we return the last save index.
          return i;
        }
      }
    }
    return 0;
  }

  /**
   * Get index into the character text at the given pixel position.
   *
   * @param font font
   * @param text text string
   * @param pixel the pixel position from the left to check for characters
   * @param size font size
   * @return index into text string
   */
  public int getCharacterIndexFromPixelPosition(
      @Nonnull final NiftyFont font,
      @Nonnull final CharSequence text,
      final int pixel,
      final float size) {
    if (pixel < 0) {
      return -1;
    }

    int current = 0;
    for (int i = 0; i < text.length(); i++) {
      char currentCharacter = text.charAt(i);
      char nextCharacter = getNextCharacter(text, i);

      int w = font.getCharacterWidth(currentCharacter, nextCharacter, size);
      if (w != -1) {
        if ((pixel >= current) && (pixel <= current + w)) {
          return i;
        }
        current += w;
      }
    }

    return text.length();
  }

  private char getNextCharacter(@Nonnull final CharSequence text, final int i) {
    char nextc = 0;
    if (i < text.length() - 1) {
      nextc = text.charAt(i + 1);
    }
    return nextc;
  }

  private char getPrevCharacter(@Nonnull final CharSequence text, final int i) {
    char prevc = 0;
    if (i > 0) {
      prevc = text.charAt(i - 1);
    }
    return prevc;
  }
}
