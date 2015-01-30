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
package de.lessvoid.nifty.api.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;

public class NiftyStyleStringConverterUnitValueArrayTest {
  private NiftyStyleStringConverterUnitValueArray converter = new NiftyStyleStringConverterUnitValueArray();

  @Test
  public void testFromStringNull() throws Exception {
    assertNull(converter.fromString(null));
  }

  @Test
  public void testFromStringEmpty() throws Exception {
    assertNull(converter.fromString(""));
  }

  @Test
  public void testFromStringSingle() throws Exception {
    assertValues(converter.fromString("20px"), new UnitValue("20px"));
  }

  @Test
  public void testFromStringTwo() throws Exception {
    assertValues(converter.fromString("20px 10px"), new UnitValue("20px"), new UnitValue("10px"));
  }

  @Test
  public void testToStringNull() throws Exception {
    assertEquals("", converter.toString(null));
  }

  @Test
  public void testToStringEmpty() throws Exception {
    assertEquals("", converter.toString(null));
  }

  @Test
  public void testToStringSingle() throws Exception {
    assertEquals("20px", converter.toString(new UnitValue[] {new UnitValue("20px")}));
  }

  @Test
  public void testToStringTwo() throws Exception {
    assertEquals("20px 50px", converter.toString(new UnitValue[] {new UnitValue("20px"), new UnitValue("50px")}));
  }

  private void assertValues(final UnitValue[] actual, final UnitValue ... expectedValues) {
    assertEquals(expectedValues.length, actual.length);
    for (int i=0; i<expectedValues.length; i++) {
      assertEquals(actual[i], expectedValues[i]);
    }
  }
}
