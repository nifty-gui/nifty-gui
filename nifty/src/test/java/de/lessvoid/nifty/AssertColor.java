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

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyMutableColor;

import static org.junit.Assert.assertEquals;

public class AssertColor {
  private static final float DELTA = 1.f / 255.f;

  private AssertColor() {
  }

  public static void assertColor(final double r, final double g, final double b, final double a, final NiftyColor c) {
    assertEquals(r, c.getRed(), DELTA);
    assertEquals(g, c.getGreen(), DELTA);
    assertEquals(b, c.getBlue(), DELTA);
    assertEquals(a, c.getAlpha(), DELTA);
  }

  public static void assertColor(final double r, final double g, final double b, final double a, final NiftyMutableColor c) {
    assertEquals(r, c.getRed(), DELTA);
    assertEquals(g, c.getGreen(), DELTA);
    assertEquals(b, c.getBlue(), DELTA);
    assertEquals(a, c.getAlpha(), DELTA);
  }

  public static void assertColor(final String expected, final NiftyColor c) {
    assertEquals(expected, c.toHexString());
  }

  public static void assertAlpha(final double expectedAlpha, final NiftyColor c) {
    assertEquals(expectedAlpha, c.getAlpha(), DELTA);
  }
}
