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

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;

public class InternalBoxConstraintWithMarginTest {
  private InternalBoxConstraints boxConstraints;

  @Before
  public void setUp() {
    boxConstraints = new InternalBoxConstraints();
  }

  @Test
  public void testMarginDefault() {
    verifyMargin("0px", "0px", "0px", "0px");
  }

  @Test
  public void testMarginLeft() {
    boxConstraints.setMarginLeft(new UnitValue("10px"));
    verifyMargin("10px", "0px", "0px", "0px");
  }

  @Test
  public void testMarginRight() {
    boxConstraints.setMarginRight(new UnitValue("10px"));
    verifyMargin("0px", "0px", "10px", "0px");
  }

  @Test
  public void testMarginTop() {
    boxConstraints.setMarginTop(new UnitValue("10px"));
    verifyMargin("0px", "10px", "0px", "0px");
  }

  @Test
  public void testMarginBottom() {
    boxConstraints.setMarginBottom(new UnitValue("10px"));
    verifyMargin("0px", "0px", "0px", "10px");
  }

  @Test
  public void testMarginTopBottom() {
    boxConstraints.setMargin(new UnitValue("10px"), new UnitValue("20px"));
    verifyMargin("20px", "10px", "20px", "10px");
  }

  @Test
  public void testMarginTopLeftRightBottom() {
    boxConstraints.setMargin(new UnitValue("10px"), new UnitValue("15px"), new UnitValue("20px"));
    verifyMargin("15px", "10px", "15px", "20px");
  }

  @Test
  public void testMarginTopRightBottomLeft() {
    boxConstraints.setMargin(
        new UnitValue("10px"), new UnitValue("15px"), new UnitValue("17px"), new UnitValue("20px"));
    verifyMargin("20px", "10px", "15px", "17px");
  }

  @Test
  public void testMargin() {
    boxConstraints.setMargin(new UnitValue("10px"));
    verifyMargin("10px", "10px", "10px", "10px");
  }

  private void verifyMargin(
      final String left,
      final String top,
      final String right,
      final String bottom) {
    assertEquals(left, boxConstraints.getMarginLeft().toString());
    assertEquals(right, boxConstraints.getMarginRight().toString());
    assertEquals(top, boxConstraints.getMarginTop().toString());
    assertEquals(bottom, boxConstraints.getMarginBottom().toString());
  }
}
