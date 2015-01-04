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
package de.lessvoid.nifty.internal.render.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyColorStop;
import de.lessvoid.nifty.api.NiftyLinearGradient;

public class LinearGradientTest {
  private static final double EPSILON = 0.000000001;

  @Test
  public void test0degrees() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0, NiftyLinearGradient.createFromAngleInDeg(0.));
    assertEquals(100.0, gradient.getStartX(), EPSILON);
    assertEquals(  0.0, gradient.getStartY(), EPSILON);
    assertEquals(100.0, gradient.getEndX(), EPSILON);
    assertEquals(100.0, gradient.getEndY(), EPSILON);
    assertTrue(gradient.getColorStops().isEmpty());
  }

  @Test
  public void test45degrees() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0, NiftyLinearGradient.createFromAngleInDeg(45.));
    assertEquals( 25.0, gradient.getStartX(), EPSILON);
    assertEquals(-25.0, gradient.getStartY(), EPSILON);
    assertEquals(175.0, gradient.getEndX(), EPSILON);
    assertEquals(125.0, gradient.getEndY(), EPSILON);
    assertTrue(gradient.getColorStops().isEmpty());
  }

  @Test
  public void test90degrees() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0, NiftyLinearGradient.createFromAngleInDeg(90.));
    assertEquals(  0.0, gradient.getStartX(), EPSILON);
    assertEquals( 50.0, gradient.getStartY(), EPSILON);
    assertEquals(200.0, gradient.getEndX(), EPSILON);
    assertEquals( 50.0, gradient.getEndY(), EPSILON);
    assertTrue(gradient.getColorStops().isEmpty());
  }

  @Test
  public void test180degrees() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0, NiftyLinearGradient.createFromAngleInDeg(180.));
    assertEquals(100.0, gradient.getStartX(), EPSILON);
    assertEquals(100.0, gradient.getStartY(), EPSILON);
    assertEquals(100.0, gradient.getEndX(), EPSILON);
    assertEquals(  0.0, gradient.getEndY(), EPSILON);
    assertTrue(gradient.getColorStops().isEmpty());
  }

  @Test
  public void testSingleColor() {
    LinearGradient gradient = new LinearGradient(
        0.0, 0.0, 200.0, 100.0,
        NiftyLinearGradient.createFromAngleInDeg(90.0)
          .addColorStop(0.0, NiftyColor.black()));
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.black()));
  }

  @Test
  public void testTwoColors() {
    LinearGradient gradient = new LinearGradient(
        0.0, 0.0, 200.0, 100.0,
        NiftyLinearGradient.createFromAngleInDeg(90.0)
          .addColorStop(0.0, NiftyColor.black())
          .addColorStop(1.0, NiftyColor.green()));
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.black()),
        new NiftyColorStop(1.0, NiftyColor.green()));
  }

  @Test
  public void testThreeColors() {
    LinearGradient gradient = new LinearGradient(
        0.0, 0.0, 200.0, 100.0,
        NiftyLinearGradient.createFromAngleInDeg(90.0)
          .addColorStop(0.0, NiftyColor.black())
          .addColorStop(0.5, NiftyColor.red())
          .addColorStop(1.0, NiftyColor.green()));
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.black()),
        new NiftyColorStop(0.5, NiftyColor.red()),
        new NiftyColorStop(1.0, NiftyColor.green()));
  }

  private void assertColorStops(final List<NiftyColorStop> colorStops, final NiftyColorStop ... stops) {
    assertEquals(colorStops.size(), stops.length);
    for (int i=0; i<stops.length; i++) {
      assertTrue(colorStops.get(i).equals(stops[i]));
      assertTrue(colorStops.get(i).getColor().equals(stops[i].getColor()));
    }
  }
}
