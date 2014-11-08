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
package de.lessvoid.nifty.internal.layout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;

public class InternalBoxConstraintsTest {

  @Test
  public void testDefaultConstructor() {
    InternalBoxConstraints box = new InternalBoxConstraints();
    assertNull(box.getX());
    assertNull(box.getY());
    assertNull(box.getWidth());
    assertNull(box.getHeight());
    assertEquals(HAlign.horizontalDefault, box.getHorizontalAlign());
    assertEquals(VAlign.verticalDefault, box.getVerticalAlign());
  }

  @Test
  public void testNormalConstructor() {
    InternalBoxConstraints box = new InternalBoxConstraints(
        new UnitValue("100px"),
        new UnitValue("200px"),
        new UnitValue("300px"),
        new UnitValue("400px"),
        HAlign.right,
        VAlign.bottom);
    assertEquals("100px", box.getX().toString());
    assertEquals("200px", box.getY().toString());
    assertEquals("300px", box.getWidth().toString());
    assertEquals("400px", box.getHeight().toString());
    assertEquals(HAlign.right, box.getHorizontalAlign());
    assertEquals(VAlign.bottom, box.getVerticalAlign());
  }

  @Test
  public void testToString() {
    InternalBoxConstraints box = new InternalBoxConstraints(
        new UnitValue("100px"),
        new UnitValue("200px"),
        new UnitValue("300px"),
        new UnitValue("400px"),
        HAlign.right,
        VAlign.bottom);
    assertTrue(box.toString().startsWith(
        "InternalBoxConstraints [x=100px, y=200px, width=300px, height=400px, " +
        "horizontalAlign=right, verticalAlign=bottom, " +
        "paddingLeft=0px, paddingRight=0px, paddingTop=0px, paddingBottom=0px, " +
        "marginLeft=0px, marginRight=0px, marginTop=0px, marginBottom=0px] "));
  }

  @Test
  public void testCopyConstructor() {
    InternalBoxConstraints box = new InternalBoxConstraints();
    InternalBoxConstraints copy = new InternalBoxConstraints(box);
    assertNull(copy.getX());
    assertNull(copy.getY());
    assertNull(copy.getWidth());
    assertNull(copy.getHeight());
    assertEquals(HAlign.horizontalDefault, copy.getHorizontalAlign());
    assertEquals(VAlign.verticalDefault, copy.getVerticalAlign());
  }

  @Test
  public void testSetter() {
    InternalBoxConstraints b = new InternalBoxConstraints();
    b.setX(new UnitValue("100px"));
    b.setY(new UnitValue("200px"));
    b.setWidth(new UnitValue("300px"));
    b.setHeight(new UnitValue("400px"));
    b.setHorizontalAlign(HAlign.right);
    b.setVerticalAlign(VAlign.bottom);
    assertEquals("100px", b.getX().toString());
    assertEquals("200px", b.getY().toString());
    assertEquals("300px", b.getWidth().toString());
    assertEquals("400px", b.getHeight().toString());
    assertEquals(HAlign.right, b.getHorizontalAlign());
    assertEquals(VAlign.bottom, b.getVerticalAlign());
  }
}
