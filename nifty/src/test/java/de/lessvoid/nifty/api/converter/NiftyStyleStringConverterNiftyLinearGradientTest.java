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
package de.lessvoid.nifty.api.converter;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyColorStop;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyLinearGradient;

public class NiftyStyleStringConverterNiftyLinearGradientTest {
  private NiftyStyleStringConverterNiftyLinearGradient converter = new NiftyStyleStringConverterNiftyLinearGradient();

  @Test
  public void testAngle() throws Exception {
    NiftyLinearGradient linearGradient = converter.fromString("linear-gradient(to bottom, red);");
    assertEquals(Math.PI, linearGradient.getAngleInRadiants());
  }

  @Test
  public void testSingleStopWithoutPos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red);",
        new NiftyColorStop(0., NiftyColor.red()));
  }

  @Test
  public void testSingleStopWithPos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red 50%);",
        new NiftyColorStop(0.5, NiftyColor.red()));
  }

  @Test
  public void testTwoStopsWithoutPos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red, green);",
        new NiftyColorStop(0., NiftyColor.red()),
        new NiftyColorStop(1., NiftyColor.green()));
  }

  @Test
  public void testTwoStopsWithPos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red 20%, green 80%);",
        new NiftyColorStop(0.2, NiftyColor.red()),
        new NiftyColorStop(0.8, NiftyColor.green()));
  }

  @Test
  public void testThreeStopsWithoutPos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red, green, blue);",
        new NiftyColorStop(0.0, NiftyColor.red()),
        new NiftyColorStop(0.5, NiftyColor.green()),
        new NiftyColorStop(1.0, NiftyColor.blue()));
  }

  @Test
  public void testThreeStopsWithMiddlePos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red, green 25%, blue);",
        new NiftyColorStop(0.0, NiftyColor.red()),
        new NiftyColorStop(0.5, NiftyColor.green()),
        new NiftyColorStop(1.0, NiftyColor.blue()));
  }

  @Test
  public void testFourStopsWithoutPos() throws Exception {
    execTest(
        "linear-gradient(to bottom, red, green, blue, yellow);",
        new NiftyColorStop(0.0, NiftyColor.red()),
        new NiftyColorStop(0.25, NiftyColor.green()),
        new NiftyColorStop(0.75, NiftyColor.blue()),
        new NiftyColorStop(1.0, NiftyColor.yellow()));
  }

  private void execTest(final String value, final NiftyColorStop ... expected) throws Exception {
    assertStops(converter.fromString(value).getColorStops(), expected);
  }

  private void assertStops(final List<NiftyColorStop> stops, final NiftyColorStop ... expected) {
    assertEquals(expected.length, stops.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i].getColor(), stops.get(i).getColor());
      assertEquals(expected[i].getStop(), stops.get(i).getStop());
    }
  }
}
