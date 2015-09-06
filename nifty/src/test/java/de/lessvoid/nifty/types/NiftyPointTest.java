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

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPoint;
import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPointWithOffset;
import static org.junit.Assert.*;

/**
 * Created by void on 06.09.15.
 */
public class NiftyPointTest {

  private static final float EPSILON = 0.000000000001f;

  @Test
  public void testRegular() {
    assertNiftyPoint(newNiftyPoint(20.f, 21.f), 20.f, 21.f);
  }

  @Test
  public void testRegularWithOffset() {
    assertNiftyPoint(newNiftyPointWithOffset(newNiftyPoint(0.f, 0.f), 20.f, 21.f), 20.f, 21.f);
  }

  @Test
  public void testToString() {
    assertEquals("20.0, 21.0", newNiftyPoint(20.f, 21.f).toString());
  }

  @Test
  public void testEquals() {
    assertEquals(newNiftyPoint(20.f, 21.f), newNiftyPoint(20.f, 21.f));
  }

  @Test
  public void testNotEquals() {
    assertNotEquals(newNiftyPoint(20.2f, 21.f), newNiftyPoint(20.f, 21.f));
  }

  @Test
  public void testEqualsWithToleranceX() {
    assertTrue(newNiftyPoint(20.001f, 21.00f).equals(newNiftyPoint(20.002f, 21.00f), 0.01f));
  }

  @Test
  public void testEqualsWithToleranceY() {
    assertTrue(newNiftyPoint(20.00f, 21.001f).equals(newNiftyPoint(20.00f, 21.002f), 0.01f));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfiniteXPosNotSupported() {
    newNiftyPoint(Float.POSITIVE_INFINITY, 20.f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfiniteXNegNotSupported() {
    newNiftyPoint(Float.NEGATIVE_INFINITY, 20.f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfiniteYPosNotSupported() {
    newNiftyPoint(20.f, Float.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfiniteYNegNotSupported() {
    newNiftyPoint(20.f, Float.NEGATIVE_INFINITY);
  }

  private void assertNiftyPoint(final NiftyPoint niftyPoint, final float expectedX, final float expectedY) {
    assertEquals(expectedX, niftyPoint.getX(), EPSILON);
    assertEquals(expectedY, niftyPoint.getY(), EPSILON);
  }
}
