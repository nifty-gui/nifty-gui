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
package de.lessvoid.niftyinternal.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InternalColorStringParserIsValidTest {
  private ColorStringParser parser = new ColorStringParser();

  @Test
  public void testShortWithoutAlpha() {
    assertTrue(parser.isValid("#f08"));
  }

  @Test
  public void testLongWithoutAlpha() {
    assertTrue(parser.isValid("#ff0088"));
  }

  @Test
  public void testShort() {
    assertTrue(parser.isValid("#f080"));
  }

  @Test
  public void testLong() {
    assertTrue(parser.isValid("#ff008800"));
  }

  @Test
  public void testInvalid() {
    assertFalse(parser.isValid("bla"));
  }

  @Test
  public void testConstantBlack() {
    assertTrue(parser.isValid("black"));
  }

  @Test
  public void testConstantSilver() {
    assertTrue(parser.isValid("silver"));
  }

  @Test
  public void testConstantGray() {
    assertTrue(parser.isValid("gray"));
  }

  @Test
  public void testConstantWhite() {
    assertTrue(parser.isValid("white"));
  }

  @Test
  public void testConstantMaroon() {
    assertTrue(parser.isValid("maroon"));
  }

  @Test
  public void testConstantRed() {
    assertTrue(parser.isValid("red"));
  }

  @Test
  public void testConstantPurple() {
    assertTrue(parser.isValid("purple"));
  }

  @Test
  public void testConstantFuchsia() {
    assertTrue(parser.isValid("fuchsia"));
  }

  @Test
  public void testConstantGreen() {
    assertTrue(parser.isValid("green"));
  }

  @Test
  public void testConstantLime() {
    assertTrue(parser.isValid("lime"));
  }

  @Test
  public void testConstantOlive() {
    assertTrue(parser.isValid("olive"));
  }

  @Test
  public void testConstantYellow() {
    assertTrue(parser.isValid("yellow"));
  }

  @Test
  public void testConstantNavy() {
    assertTrue(parser.isValid("navy"));
  }

  @Test
  public void testConstantBlue() {
    assertTrue(parser.isValid("blue"));
  }

  @Test
  public void testConstantTeal() {
    assertTrue(parser.isValid("teal"));
  }

  @Test
  public void testConstantAqua() {
    assertTrue(parser.isValid("aqua"));
  }
}
