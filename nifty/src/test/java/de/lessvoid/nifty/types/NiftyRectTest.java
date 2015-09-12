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
import static de.lessvoid.nifty.types.NiftyRect.newNiftyRect;
import static org.junit.Assert.*;

/**
 * Created by void on 06.09.15.
 */
public class NiftyRectTest {

  @Test
  public void testRegular() {
    NiftyRect rect = newNiftyRect(newNiftyPoint(20.f, 21.f), NiftySize.newNiftySize(10.f, 11.f));
    assertEquals(newNiftyPoint(20.f, 21.f), rect.getOrigin());
    assertEquals(NiftySize.newNiftySize(10.f, 11.f), rect.getSize());
  }

  @Test
  public void testToString() {
    NiftyRect rect = newNiftyRect(newNiftyPoint(20.f, 21.f), NiftySize.newNiftySize(10.f, 11.f));
    assertEquals("10.0 x 11.0 at 20.0, 21.0", rect.toString());
  }

  @Test
  public void testEquals() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(20.f, 21.f), NiftySize.newNiftySize(10.f, 11.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(20.f, 21.f), NiftySize.newNiftySize(10.f, 11.f));
    assertEquals(rect1, rect2);
  }

  @Test
  public void testEqualsWithTolerance() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(20.00f, 21.00f), NiftySize.newNiftySize(10.01f, 11.00f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(20.00f, 21.00f), NiftySize.newNiftySize(10.02f, 11.00f));
    assertTrue(rect1.equals(rect2, 0.1f));
  }

  @Test
  public void testNotOverlappingLeft() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint( 90.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    assertFalse(rect1.isOverlapping(rect2));
  }

  @Test
  public void testOverlappingLeft() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint( 91.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    assertTrue(rect1.isOverlapping(rect2));
  }

  @Test
  public void testNotOverlappingRight() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(110.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    assertFalse(rect1.isOverlapping(rect2));
  }

  @Test
  public void testOverlappingRight() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(109.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    assertTrue(rect1.isOverlapping(rect2));
  }

  @Test
  public void testNotOverlappingTop() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(100.f,  90.f), NiftySize.newNiftySize(10.f, 10.f));
    assertFalse(rect1.isOverlapping(rect2));
  }

  @Test
  public void testOverlappingTop() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(100.f,  91.f), NiftySize.newNiftySize(10.f, 10.f));
    assertTrue(rect1.isOverlapping(rect2));
  }

  @Test
  public void testNotOverlappingBottom() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(100.f, 110.f), NiftySize.newNiftySize(10.f, 10.f));
    assertFalse(rect1.isOverlapping(rect2));
  }

  @Test
  public void testOverlappingBottom() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(100.f, 109.f), NiftySize.newNiftySize(10.f, 10.f));
    assertTrue(rect1.isOverlapping(rect2));
  }

  @Test
  public void testTotallyOverlapping() {
    NiftyRect rect1 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    NiftyRect rect2 = newNiftyRect(newNiftyPoint(100.f, 100.f), NiftySize.newNiftySize(10.f, 10.f));
    assertTrue(rect1.isOverlapping(rect2));
  }
}
