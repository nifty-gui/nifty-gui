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

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;

public class InternalBoxConstraintWithPaddingTest {
  private InternalBoxConstraints boxConstraints;

  @Before
  public void setUp() {
    boxConstraints = new InternalBoxConstraints();
  }

  @Test
  public void testPaddingDefault() {
    verifyPadding("0px", "0px", "0px", "0px");
  }

  @Test
  public void testPaddingLeft() {
    boxConstraints.setPaddingLeft(new UnitValue("10px"));
    verifyPadding("10px", "0px", "0px", "0px");
  }

  @Test
  public void testPaddingRight() {
    boxConstraints.setPaddingRight(new UnitValue("10px"));
    verifyPadding("0px", "0px", "10px", "0px");
  }

  @Test
  public void testPaddingTop() {
    boxConstraints.setPaddingTop(new UnitValue("10px"));
    verifyPadding("0px", "10px", "0px", "0px");
  }

  @Test
  public void testPaddingBottom() {
    boxConstraints.setPaddingBottom(new UnitValue("10px"));
    verifyPadding("0px", "0px", "0px", "10px");
  }

  @Test
  public void testPaddingTopBottom() {
    boxConstraints.setPadding(new UnitValue("10px"), new UnitValue("20px"));
    verifyPadding("20px", "10px", "20px", "10px");
  }

  @Test
  public void testPaddingTopLeftRightBottom() {
    boxConstraints.setPadding(new UnitValue("10px"), new UnitValue("15px"), new UnitValue("20px"));
    verifyPadding("15px", "10px", "15px", "20px");
  }

  @Test
  public void testPaddingTopRightBottomLeft() {
    boxConstraints.setPadding(
        new UnitValue("10px"), new UnitValue("15px"), new UnitValue("17px"), new UnitValue("20px"));
    verifyPadding("20px", "10px", "15px", "17px");
  }

  @Test
  public void testPadding() {
    boxConstraints.setPadding(new UnitValue("10px"));
    verifyPadding("10px", "10px", "10px", "10px");
  }

  private void verifyPadding(
      final String left,
      final String top,
      final String right,
      final String bottom) {
    assertEquals(left, boxConstraints.getPaddingLeft().toString());
    assertEquals(right, boxConstraints.getPaddingRight().toString());
    assertEquals(top, boxConstraints.getPaddingTop().toString());
    assertEquals(bottom, boxConstraints.getPaddingBottom().toString());
  }
}
