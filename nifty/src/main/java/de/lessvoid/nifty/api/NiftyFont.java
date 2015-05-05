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
package de.lessvoid.nifty.api;

import org.jglfont.JGLFont;

import de.lessvoid.nifty.internal.accessor.NiftyFontAccessor;

public class NiftyFont {
  private final JGLFont font;
  private final String name;

  NiftyFont(final JGLFont font, final String name) {
    this.font = font;
    this.name = name;
  }

  JGLFont getJGLFont() {
    return font;
  }

  /**
   * Return the width of the given text String in px using.
   *
   * @param text the String to get the width for
   * @return the width in px of the String
   */
  public int getWidth(final String text) {
    return font.getStringWidth(text);
  }

  /**
   * Return the height of the font.
   *
   * @return the height in px
   */
  public int getHeight() {
    return font.getHeight();
  }

  /**
   * Return the width in px of the given character including kerning information taking the next character into account.
   *
   * @param currentCharacter current character
   * @param nextCharacter next character
   * @param size font size
   * @return width of the character or {@code -1} when no information for the character is available
   */
  public int getCharacterWidth(final char currentCharacter, final char nextCharacter, final float size) {
    return font.getCharacterWidth(currentCharacter, nextCharacter, size);
  }

  /**
   * Return the filename this font originates from.
   * @return the filename of the font
   */
  public String getName() {
    return name;
  }

  static {
    NiftyFontAccessor.DEFAULT = new InternalNiftyFontAccessorImpl();
  }
}
