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

import static de.lessvoid.nifty.AssertColor.assertColor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.lessvoid.nifty.types.NiftyColor;
import org.junit.Test;

public class NiftyColorTest {

  @Test
  public void testConstruct() {
    NiftyColor c = new NiftyColor(1.f, .5f, .3f, .75f);
    assertColor(1.f, .5f, .3f, .75f, c);
  }

  @Test
  public void testCopy() {
    NiftyColor c = new NiftyColor(new NiftyColor(1.f, .5f, .3f, .75f));
    assertColor(1.f, .5f, .3f, .75f, c);
  }

  @Test
  public void testBLACK() {
    assertColor(0.f, 0.f, 0.f, 1.f, NiftyColor.black());
  }

  @Test
  public void testWHITE() {
    assertColor(1.f, 1.f, 1.f, 1.f, NiftyColor.white());
  }

  @Test
  public void testRED() {
    assertColor(1.f, 0.f, 0.f, 1.f, NiftyColor.red());
  }

  @Test
  public void testGREEN() {
    assertColor(0.f, 0.5f, 0.f, 1.f, NiftyColor.green());
  }

  @Test
  public void testBLUE() {
    assertColor(0.f, 0.f, 1.f, 1.f, NiftyColor.blue());
  }

  @Test
  public void testYELLOW() {
    assertColor(1.f, 1.f, 0.f, 1.f, NiftyColor.yellow());
  }

  @Test
  public void testNONE() {
    assertColor(0.f, 0.f, 0.f, 0.f, NiftyColor.none());
  }

  @Test
  public void testLongColor() {
    assertColor(1.f, 0.f, 0.f, 1.f, NiftyColor.fromString("#ff0000ff"));
  }

  @Test
  public void testShortColor() {
    assertColor(1.f, 0.f, 0.f, 1.f, NiftyColor.fromString("#f00f"));
  }

  @Test
  public void testFromColorWithAlpha() {
    assertColor(1.f, 0.f, 0.f, .45f, NiftyColor.fromColorWithAlpha(NiftyColor.red(), 0.45f));
  }

  @Test
  public void testFromInt() {
    assertColor(1.f, 0.f, 1.f, 0.f, NiftyColor.fromInt(0xFF00FF00));
  }

  @Test
  public void testFromHSV() {
    assertColor(.5f, 0.25f, 0.25f, 1.f, NiftyColor.fromHSV(0.5f, 0.5f, 0.5f));
  }

  @Test
  public void testFromRandom() {
    NiftyColor.randomColor();
  }

  @Test
  public void testToString() {
    assertEquals("#0000ffff {0.0, 0.0, 1.0, 1.0}", NiftyColor.blue().toString());
  }

  @Test
  public void testToHexString() {
    assertEquals("#0000ffff", NiftyColor.blue().toHexString());
  }

  @Test
  public void testHashCode() {
    assertEquals(-1173481599L, NiftyColor.red().hashCode());
  }

  @Test
  public void testEquals() {
    NiftyColor a = NiftyColor.none();
    NiftyColor b = new NiftyColor(0.f, 0.f, 0.f, 0.f);
    assertTrue(a.equals(b));
  }

  @Test
  public void testEqualsSame() {
    NiftyColor a = NiftyColor.none();
    NiftyColor b = a;
    assertTrue(a.equals(b));
  }

  @Test
  public void testIsColor() {
    assertTrue(NiftyColor.isColor("#f00f"));
  }
}
