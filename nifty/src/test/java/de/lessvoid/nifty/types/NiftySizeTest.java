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
package de.lessvoid.nifty.types;

import org.junit.Test;

import static de.lessvoid.nifty.types.NiftySize.max;
import static de.lessvoid.nifty.types.NiftySize.newNiftySize;
import static org.junit.Assert.*;

/**
 * Created by void on 06.09.15.
 */
public class NiftySizeTest {
  private static final float EPSILON = 0.0000000001f;

  @Test
  public void testRegular() {
    assertSize(10.f, 11.f, newNiftySize(10.f, 11.f));
  }

  @Test
  public void testToString() {
    assertEquals("10.0 x 11.0", newNiftySize(10.f, 11.f).toString());
  }

  @Test
  public void testEquals() {
    assertEquals(newNiftySize(10.f, 11.f), newNiftySize(10.f, 11.f));
  }

  @Test
  public void testEqualsWithToleranceX() {
    assertTrue(newNiftySize(10.01f, 11.f).equals(newNiftySize(10.02f, 11.f), 0.1f));
  }

  @Test
  public void testEqualsWithToleranceY() {
    assertTrue(newNiftySize(10.f, 11.01f).equals(newNiftySize(10.f, 11.02f), 0.1f));
  }

  @Test
  public void testValid() {
    assertFalse(newNiftySize(10.f, 11.f).isInvalid());
  }

  @Test
  public void testInvalidWidth() {
    assertTrue(newNiftySize(Float.NaN, 11.f).isInvalid());
  }

  @Test
  public void testInvalidHeight() {
    assertTrue(newNiftySize(10.f, Float.NaN).isInvalid());
  }

  @Test
  public void testFinite() {
    assertFalse(newNiftySize(10.f, 11.f).isInfinite());
  }

  @Test
  public void testPosInfiniteWidth() {
    assertTrue(newNiftySize(Float.POSITIVE_INFINITY, 11.f).isInfinite());
  }

  @Test
  public void testPosInfiniteHeight() {
    assertTrue(newNiftySize(10.f, Float.POSITIVE_INFINITY).isInfinite());
  }

  @Test
  public void testNegInfiniteWidth() {
    assertTrue(newNiftySize(Float.NEGATIVE_INFINITY, 11.f).isInfinite());
  }

  @Test
  public void testNegInfiniteHeight() {
    assertTrue(newNiftySize(10.f, Float.NEGATIVE_INFINITY).isInfinite());
  }

  @Test
  public void testMaxEquals() {
    NiftySize a = newNiftySize(10.f, 11.f);
    NiftySize b = newNiftySize(10.f, 11.f);
    assertEquals(a, max(a, b));
  }

  @Test
  public void testMaxAGreaterBonX() {
    NiftySize a = newNiftySize(12.f, 11.f);
    NiftySize b = newNiftySize(10.f, 11.f);
    assertEquals(a, max(a, b));
  }

  @Test
  public void testMaxAGreaterBonY() {
    NiftySize a = newNiftySize(10.f, 12.f);
    NiftySize b = newNiftySize(10.f, 11.f);
    assertEquals(a, max(a, b));
  }

  @Test
  public void testMaxBGreaterAonX() {
    NiftySize a = newNiftySize(10.f, 11.f);
    NiftySize b = newNiftySize(12.f, 11.f);
    assertEquals(b, max(a, b));
  }

  @Test
  public void testMaxBGreaterAonY() {
    NiftySize a = newNiftySize(10.f, 11.f);
    NiftySize b = newNiftySize(10.f, 12.f);
    assertEquals(b, max(a, b));
  }

  @Test
  public void testMax() {
    NiftySize a = newNiftySize(12.f, 11.f);
    NiftySize b = newNiftySize(10.f, 12.f);
    assertEquals(newNiftySize(12.f, 12.f), max(a, b));
  }

  private void assertSize(float expectedWidth, float expectedHeight, final NiftySize niftySize) {
    assertEquals(expectedWidth, niftySize.getWidth(), EPSILON);
    assertEquals(expectedHeight, niftySize.getHeight(), EPSILON);
  }
}