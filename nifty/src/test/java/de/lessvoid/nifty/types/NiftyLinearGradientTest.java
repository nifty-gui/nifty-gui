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

import de.lessvoid.nifty.spi.NiftyRenderDevice.ColorStop;
import de.lessvoid.niftyinternal.common.InternalNiftyColorStop;
import de.lessvoid.niftyinternal.render.batch.LinearGradient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NiftyLinearGradientTest {
  private static final float EPSILON = 0.0000000001f;

  @Test
  public void testCopyConstructor() {
    NiftyLinearGradient source = createFromAngleInRad(Math.PI/2);
    source.addColorStop(0.0, NiftyColor.aqua());
    assertLinearGradient(source, 0.0, 50.0, 200.0, 50.0, new InternalNiftyColorStop(0.0, NiftyColor.aqua()));

    NiftyLinearGradient copy = new NiftyLinearGradient(source);
    assertLinearGradient(copy, 0.0, 50.0, 200.0, 50.0, new InternalNiftyColorStop(0.0, NiftyColor.aqua()));
  }

  @Test
  public void testCreateFromPoints() {
    NiftyLinearGradient gradient = createFromPoints(0.0, 0.0, 100.0, 0.0);
    assertLinearGradient(gradient, 0.0, 0.0, 100.0, 0.0);
  }

  @Test
  public void testCreateFromPoints90() {
    NiftyLinearGradient gradient = createFromPoints(0.0, 0.0, 0.0, 100.0);
    assertLinearGradient(gradient, 0.0, 0.0, 0.0, 100.0);
  }

  @Test
  public void testCreateFromAngleInRad() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    assertLinearGradient(gradient, 100.0, 100.0, 100.0, 0.0);
  }

  @Test
  public void testCreateFromAngleInRad90() {
    NiftyLinearGradient gradient = createFromAngleInRad(Math.PI/2);
    assertLinearGradient(gradient, 0.0, 50.0, 200.0, 50.0);
  }

  @Test
  public void testCreateFromAngleInRad180() {
    NiftyLinearGradient gradient = createFromAngleInRad(Math.PI);
    assertLinearGradient(gradient, 100.0, 0.0, 100.0, 100.0);
  }

  @Test
  public void testCreateFromAngleInRadMinus90() {
    NiftyLinearGradient gradient = createFromAngleInRad(-Math.PI/2);
    assertLinearGradient(gradient, 200.0, 50.0, 0.0, 50.0);
  }

  @Test
  public void testCreateFromAngleInDegree() {
    NiftyLinearGradient gradient = createFromAngleInDegree(0.0);
    assertLinearGradient(gradient, 100.0, 100.0, 100.0, 0.0);
  }

  @Test
  public void testCreateFromAngleInDegree90() {
    NiftyLinearGradient gradient = createFromAngleInDegree(90.0);
    assertLinearGradient(gradient, 0.0, 50.0, 200.0, 50.0);
  }

  @Test
  public void testCreateFromAngleInDegree180() {
    NiftyLinearGradient gradient = createFromAngleInDegree(180.0);
    assertLinearGradient(gradient, 100.0, 0.0, 100.0, 100.0);
  }

  @Test
  public void testCreateFromAngleInDegreeMinus90() {
    NiftyLinearGradient gradient = createFromAngleInDegree(-90.0);
    assertLinearGradient(gradient, 200.0, 50.0, 0.0, 50.0);
  }

  @Test
  public void testSingleColor() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    gradient.addColorStop(0.0, NiftyColor.black());
    assertLinearGradient(gradient, 100.0, 100.0, 100.0, 0.0, new InternalNiftyColorStop(0.0, NiftyColor.black()));
  }

  @Test
  public void testTwoColors() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(1.0, NiftyColor.green());
    assertLinearGradient(
      gradient, 100.0, 100.0, 100.0, 0.0,
      new InternalNiftyColorStop(0.0, NiftyColor.black()),
      new InternalNiftyColorStop(1.0, NiftyColor.green()));
  }

  @Test
  public void testThreeColors() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(0.5, NiftyColor.red());
    gradient.addColorStop(1.0, NiftyColor.green());
    assertLinearGradient(
      gradient, 100.0, 100.0, 100.0, 0.0,
      new InternalNiftyColorStop(0.0, NiftyColor.black()),
      new InternalNiftyColorStop(0.5, NiftyColor.red()),
      new InternalNiftyColorStop(1.0, NiftyColor.green()));
  }

  @Test
  public void testReplaceColor() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(0.0, NiftyColor.blue());
    assertLinearGradient(
      gradient, 100.0, 100.0, 100.0, 0.0,
      new InternalNiftyColorStop(0.0, NiftyColor.blue()));
  }

  @Test
  public void testToString() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(0.5, NiftyColor.red());
    gradient.addColorStop(1.0, NiftyColor.green());
    assertEquals(
      "NiftyLinearGradient(" +
        "startX[0.0] " +
        "startY[0.0] " +
        "endX[0.0] " +
        "endY[0.0] " +
        "angleInRadiants[0.0] " +
        "angleMode[true] " +
        "colorStops [" +
          "0.0: #000000ff {0.0, 0.0, 0.0, 1.0}, " +
          "0.5: #ff0000ff {1.0, 0.0, 0.0, 1.0}, " +
          "1.0: #008000ff {0.0, 0.501960813999176, 0.0, 1.0}])", gradient.toString());
  }

  @Test
  public void testToStringNoColorStops() {
    NiftyLinearGradient gradient = createFromAngleInRad(0.0);
    assertEquals(
      "NiftyLinearGradient(" +
        "startX[0.0] " +
        "startY[0.0] " +
        "endX[0.0] " +
        "endY[0.0] " +
        "angleInRadiants[0.0] " +
        "angleMode[true] " +
        "colorStops [])", gradient.toString());
  }

  @Test
  public void testToStringPointMode() {
    NiftyLinearGradient gradient = createFromPoints(1.0, 2.0, 3.0, 4.0);
    assertEquals(
      "NiftyLinearGradient(" +
        "startX[1.0] " +
        "startY[2.0] " +
        "endX[3.0] " +
        "endY[4.0] " +
        "angleInRadiants[0.0] " +
        "angleMode[false] " +
        "colorStops [])", gradient.toString());
  }

  private void assertLinearGradient(
    final NiftyLinearGradient gradient,
    final double startX,
    final double startY,
    final double endX,
    final double endY,
    final InternalNiftyColorStop ... stops) {
    LinearGradient g = gradient.asLinearGradient(0.0, 0.0, 200.0, 100.0);
    assertEquals("startX", startX, g.getStartX(), EPSILON);
    assertEquals("startY", startY, g.getStartY(), EPSILON);
    assertEquals("endX", endX, g.getEndX(), EPSILON);
    assertEquals("endY", endY, g.getEndY(), EPSILON);
    assertColorStops(g.getColorStops(), stops);
  }

  private NiftyLinearGradient createFromPoints(final double x0, final double y0, final double x1, final double y1) {
    return NiftyLinearGradient.create(x0, y0, x1, y1);
  }

  private NiftyLinearGradient createFromAngleInRad(final double angleRad) {
    return NiftyLinearGradient.createFromAngleInRad(angleRad);
  }

  private NiftyLinearGradient createFromAngleInDegree(final double angleInDegree) {
    return NiftyLinearGradient.createFromAngleInDeg(angleInDegree);
  }

  private void assertColorStops(final List<ColorStop> colorStops, final InternalNiftyColorStop... stops) {
    assertEquals(colorStops.size(), stops.length);
    for (int i=0; i<stops.length; i++) {
      assertTrue(colorStops.get(i).equals(stops[i]));
      assertTrue(colorStops.get(i).getColor().equals(stops[i].getColor()));
    }
  }
}
