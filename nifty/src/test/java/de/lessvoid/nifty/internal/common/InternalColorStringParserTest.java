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

import static de.lessvoid.nifty.AssertColor.assertColor;

import org.junit.Test;

import de.lessvoid.nifty.internal.common.ColorStringParser;

public class InternalColorStringParserTest {
  private ColorStringParser parser = new ColorStringParser();

  @Test
  public void testShortWithoutAlpha() {
    assertColor(1.f, 0.f, 0x88 / 255.f, 1.f, parser.fromString("#f08"));
  }

  @Test
  public void testLongWithoutAlpha() {
    assertColor(1.f, 0.f, 0x88 / 255.f, 1.f, parser.fromString("#ff0088"));
  }

  @Test
  public void testShort() {
    assertColor(1.f, 0.f, 0x88 / 255.f, 0.f, parser.fromString("#f080"));
  }

  @Test
  public void testLong() {
    assertColor(1.f, 0.f, 0x88 / 255.f, 0.f, parser.fromString("#ff008800"));
  }

  @Test
  public void testInvalid() {
    assertColor(1.f, 1.f, 1.f, 1.f, parser.fromString("bla"));
  }

  @Test
  public void testConstantBlack() {
    assertColor("#000000ff", parser.fromString("black"));
    assertColor("#000000ff", parser.fromString("BLACK"));
  }

  @Test
  public void testConstantSilver() {
    assertColor("#c0c0c0ff", parser.fromString("silver"));
    assertColor("#c0c0c0ff", parser.fromString("SILVER"));
  }

  @Test
  public void testConstantGray() {
    assertColor("#808080ff", parser.fromString("gray"));
    assertColor("#808080ff", parser.fromString("GRAY"));
  }

  @Test
  public void testConstantWhite() {
    assertColor("#ffffffff", parser.fromString("white"));
    assertColor("#ffffffff", parser.fromString("WHITE"));
  }

  @Test
  public void testConstantMaroon() {
    assertColor("#800000ff", parser.fromString("maroon"));
    assertColor("#800000ff", parser.fromString("MAROON"));
  }

  @Test
  public void testConstantRed() {
    assertColor("#ff0000ff", parser.fromString("red"));
    assertColor("#ff0000ff", parser.fromString("RED"));
  }

  @Test
  public void testConstantPurple() {
    assertColor("#800080ff", parser.fromString("purple"));
    assertColor("#800080ff", parser.fromString("PURPLE"));
  }

  @Test
  public void testConstantFuchsia() {
    assertColor("#ff00ffff", parser.fromString("fuchsia"));
    assertColor("#ff00ffff", parser.fromString("FUCHSIA"));
  }

  @Test
  public void testConstantGreen() {
    assertColor("#008000ff", parser.fromString("green"));
    assertColor("#008000ff", parser.fromString("GREEN"));
  }

  @Test
  public void testConstantLime() {
    assertColor("#00ff00ff", parser.fromString("lime"));
    assertColor("#00ff00ff", parser.fromString("LIME"));
  }

  @Test
  public void testConstantOlive() {
    assertColor("#808000ff", parser.fromString("olive"));
    assertColor("#808000ff", parser.fromString("OLIVE"));
  }

  @Test
  public void testConstantYellow() {
    assertColor("#ffff00ff", parser.fromString("yellow"));
    assertColor("#ffff00ff", parser.fromString("YELLOW"));
  }

  @Test
  public void testConstantNavy() {
    assertColor("#000080ff", parser.fromString("navy"));
    assertColor("#000080ff", parser.fromString("NAVY"));
  }

  @Test
  public void testConstantBlue() {
    assertColor("#0000ffff", parser.fromString("blue"));
    assertColor("#0000ffff", parser.fromString("BLUE"));
  }

  @Test
  public void testConstantTeal() {
    assertColor("#008080ff", parser.fromString("teal"));
    assertColor("#008080ff", parser.fromString("TEAL"));
  }

  @Test
  public void testConstantAqua() {
    assertColor("#00ffffff", parser.fromString("aqua"));
    assertColor("#00ffffff", parser.fromString("AQUA"));
  }
}
