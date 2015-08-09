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
package de.lessvoid.nifty.internal.style.specialparser;

import de.lessvoid.nifty.internal.style.specialparser.LinearGradientParser.ColorStop;
import de.lessvoid.nifty.internal.style.specialparser.LinearGradientParser.Result;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LinearGradientParserTest {
  private final LinearGradientParser parser = new LinearGradientParser();

  @Test
  public void testTwoColorstops() throws Exception {
    execTest("linear-gradient(red, green);", 180, new ColorStop("red"), new ColorStop("green"));
  }

  @Test
  public void testSingleColorstops() throws Exception {
    execTest("linear-gradient(red);", 180, new ColorStop("red"));
  }

  @Test
  public void testSingleColorstopsColorNumber() throws Exception {
    execTest("linear-gradient(#800000);", 180, new ColorStop("#800000"));
  }

  @Test
  public void testSingleColorstopsColorAlphaNumber() throws Exception {
    execTest("linear-gradient(#ff0000);", 180, new ColorStop("#ff0000"));
  }

  @Test
  public void testSingleColorstopsColorMixedNumber() throws Exception {
    execTest("linear-gradient(#80ffff);", 180, new ColorStop("#80ffff"));
  }

  @Test
  public void testSingleColorstopsColorTotallyMixedNumber() throws Exception {
    execTest("linear-gradient(#a0b29f 10%);", 180, new ColorStop("#a0b29f", 0.1));
  }
/* FIXME
  @Test
  public void testSingleColorstopsColorTotallyMixedNumber2() throws Exception {
    execTest("linear-gradient(#200b30 10%);", 180, new ColorStop("#200b30", 0.1));
  }
*/
  @Test
  public void testSingleColorstopsColorMixedNumberWithPercent() throws Exception {
    execTest("linear-gradient(#80ffff -23%);", 180, new ColorStop("#80ffff", -0.23));
  }

  @Test
  public void testSingleColorstopsColorMixedNumberWithPercent2() throws Exception {
    execTest("linear-gradient(#80ffff 23%);", 180, new ColorStop("#80ffff", 0.23));
  }

  @Test
  public void testSingleColorstopWithPercent() throws Exception {
    execTest("linear-gradient(red 10%);", 180, new ColorStop("red", 0.1));
  }

  @Test
  public void testSingleColorstopWithDecimal() throws Exception {
    execTest("linear-gradient(red 10.5%);", 180, new ColorStop("red", 0.105));
  }

  @Test
  public void testSingleColorstopWithNegativePercent() throws Exception {
    execTest("linear-gradient(red -10%);", 180, new ColorStop("red", -0.1));
  }

  @Test
  public void testTwoColorstopsWebColorsWithAlpha() throws Exception {
    execTest("linear-gradient(#f000, #ff00aaff);", 180, new ColorStop("#f000"), new ColorStop("#ff00aaff"));
  }

  @Test
  public void testTwoColorstopsWebColorsNoAlpha() throws Exception {
    execTest("linear-gradient(#f00, #ff00aa);", 180, new ColorStop("#f00"), new ColorStop("#ff00aa"));
  }

  @Test
  public void testThreeColorstops() throws Exception {
    execTest("linear-gradient(red, green, blue);", 180, new ColorStop("red"), new ColorStop("green"), new ColorStop("blue"));
  }

  @Test
  public void testFourColorstops() throws Exception {
    execTest("linear-gradient(red, green, blue, black);", 180, new ColorStop("red"), new ColorStop("green"), new ColorStop("blue"), new ColorStop("black"));
  }

  @Test
  public void testFourColorstopsWithSteps() throws Exception {
    execTest("linear-gradient(red 0%, green, blue, black 100%);", 180, new ColorStop("red", 0.), new ColorStop("green"), new ColorStop("blue"), new ColorStop("black", 1.0));
  }

  @Test
  public void testStartsWithAngleDeg() throws Exception {
    execTest("linear-gradient(10deg, red);", 10, new ColorStop("red"));
  }

  @Test
  public void testStartsWithNegativeDecimalAngleDeg() throws Exception {
    execTest("linear-gradient(-10.5deg, red);", -10.5, new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleGrad() throws Exception {
    execTest("linear-gradient(10grad, red);", 9, new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleRad() throws Exception {
    execTest("linear-gradient(3.14rad, red);", toGrad(3.14), new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleRadNoLeadingZero() throws Exception {
    execTest("linear-gradient(.14rad, red);", toGrad(.14), new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleTurn() throws Exception {
    execTest("linear-gradient(0.5turn, red);", 180, new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleDegUpperCase() throws Exception {
    execTest("linear-gradient(10DEG, red);", 10, new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleGradUpperCase() throws Exception {
    execTest("linear-gradient(10GRAD, red);", 9, new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleRadUpperCase() throws Exception {
    execTest("linear-gradient(3.14RAD, red);", toGrad(3.14), new ColorStop("red"));
  }

  @Test
  public void testStartsWithAngleTurnUpperCase() throws Exception {
    execTest("linear-gradient(1TURN, red);", 360., new ColorStop("red"));
  }

  @Test
  public void testWithLeft() throws Exception {
    execTest("linear-gradient(to left, red);", 270., new ColorStop("red"));
  }

  @Test
  public void testWithLeftTop() throws Exception {
    execTest("linear-gradient(to left top, red);", 315, new ColorStop("red"));
  }

  @Test
  public void testWithTopLeft() throws Exception {
    execTest("linear-gradient(to top left, red);", 315, new ColorStop("red"));
  }

  @Test
  public void testWithLeftBottom() throws Exception {
    execTest("linear-gradient(to left bottom, red);", 225, new ColorStop("red"));
  }

  @Test
  public void testWithBottomLeft() throws Exception {
    execTest("linear-gradient(to bottom left, red);", 225, new ColorStop("red"));
  }

  @Test
  public void testWithRight() throws Exception {
    execTest("linear-gradient(to right, red);", 90, new ColorStop("red"));
  }

  @Test
  public void testWithRightTop() throws Exception {
    execTest("linear-gradient(to right top, red);", 45, new ColorStop("red"));
  }

  @Test
  public void testWithRightBottom() throws Exception {
    execTest("linear-gradient(to right bottom, red);", 135, new ColorStop("red"));
  }

  @Test
  public void testWithBottom() throws Exception {
    execTest("linear-gradient(to bottom, red);", 180, new ColorStop("red"));
  }

  @Test
  public void testWithTopRight() throws Exception {
    execTest("linear-gradient(to top right, red);", 45, new ColorStop("red"));
  }

  private void execTest(final String string, final double expectedAngle, final ColorStop ... stops) throws Exception {
    Result result = parser.parse(string);
    assertAngle(expectedAngle, result);
    assertStops(stops, result.getStops());
  }

  private void assertAngle(final double expectedAngle, final Result result) {
    assertEquals(toRad(expectedAngle), result.getAngleInRadiants(), 0.0001);
  }

  private void assertStops(final ColorStop[] expectedStops, List<ColorStop> actualStops) {
    assertEquals(expectedStops.length, actualStops.size());
    for (int i=0; i<expectedStops.length; i++) {
      assertEquals(expectedStops[i], actualStops.get(i));
    }
  }

  private double toRad(final double grad) {
    return Math.PI * 2 * grad / 360.;
  }

  private double toGrad(final double rad) {
    return 360. * rad / (Math.PI * 2.);
  }
}
