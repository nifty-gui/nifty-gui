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
package de.lessvoid.nifty.internal.layout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.internal.common.Box;

public class InternalBoxTest {

  @Test
  public void testDefaultConstructor() {
    Box box = new Box();
    assertEquals(0, box.getX());
    assertEquals(0, box.getY());
    assertEquals(0, box.getWidth());
    assertEquals(0, box.getHeight());
  }

  @Test
  public void testHashcode() {
    Box box = new Box();
    assertEquals(923521L, box.hashCode());
  }

  @Test
  public void testNormalConstructor() {
    Box box = new Box(100, 100, 200, 200);
    assertEquals(100, box.getX());
    assertEquals(100, box.getY());
    assertEquals(200, box.getWidth());
    assertEquals(200, box.getHeight());
  }

  @Test
  public void testToString() {
    Box box = new Box(100, 100, 200, 200);
    assertTrue(box.toString().startsWith("InternalBox [x=100, y=100, width=200, height=200]"));
  }

  @Test
  public void testCopyConstructor() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testFrom() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box();
    copy.from(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testSetter() {
    Box copy = new Box();
    copy.setX(100);
    copy.setY(200);
    copy.setWidth(300);
    copy.setHeight(400);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testSetDimenstion() {
    Box copy = new Box();
    copy.setDimension(300, 400);
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testEquals() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box(box);
    assertTrue(box.equals(copy));
  }

  @Test
  public void testEqualsSame() {
    Box box = new Box(100, 200, 300, 400);
    assertTrue(box.equals(box));
  }

  @Test
  public void testNotEqualsNull() {
    Box box = new Box(100, 200, 300, 400);
    assertFalse(box.equals(null));
  }

  @Test
  public void testNotEqualsWrongClass() {
    Box box = new Box(100, 200, 300, 400);
    assertFalse(box.equals(new Object()));
  }

  @Test
  public void testNotEqualsWrongX() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(101, 200, 300, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongY() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 201, 300, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongWidth() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 200, 301, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongHeight() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 200, 300, 401);
    assertFalse(a.equals(b));
  }
}
