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
package de.lessvoid.niftyinternal.render.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.lessvoid.nifty.spi.NiftyRenderDevice.ColorStop;
import org.junit.Test;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.niftyinternal.common.InternalNiftyColorStop;
import de.lessvoid.nifty.types.NiftyLinearGradient;

public class LinearGradientTest {
  private static final double EPSILON = 0.001;

  @Test
  public void testWithoutColorStops() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0, new TreeSet<InternalNiftyColorStop>());
    assertEquals(  0.0, gradient.getStartX(), EPSILON);
    assertEquals(  0.0, gradient.getStartY(), EPSILON);
    assertEquals(200.0, gradient.getEndX(), EPSILON);
    assertEquals(100.0, gradient.getEndY(), EPSILON);
    assertTrue(gradient.getColorStops().isEmpty());
  }

  @Test
  public void testSingleColor() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0,
        makeColorStops(new InternalNiftyColorStop(0.0, NiftyColor.black())));
    assertColorStops(gradient.getColorStops(), new InternalNiftyColorStop(0.0, NiftyColor.black()));
  }

  @Test
  public void testTwoColors() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0,
        makeColorStops(
          new InternalNiftyColorStop(0.0, NiftyColor.black()),
          new InternalNiftyColorStop(1.0, NiftyColor.green())));
    assertColorStops(
        gradient.getColorStops(),
        new InternalNiftyColorStop(0.0, NiftyColor.black()),
        new InternalNiftyColorStop(1.0, NiftyColor.green()));
  }

  @Test
  public void testThreeColors() {
    LinearGradient gradient = new LinearGradient(0.0, 0.0, 200.0, 100.0,
        makeColorStops(
          new InternalNiftyColorStop(0.0, NiftyColor.black()),
          new InternalNiftyColorStop(0.5, NiftyColor.red()),
          new InternalNiftyColorStop(1.0, NiftyColor.green())));
    assertColorStops(
        gradient.getColorStops(),
        new InternalNiftyColorStop(0.0, NiftyColor.black()),
        new InternalNiftyColorStop(0.5, NiftyColor.red()),
        new InternalNiftyColorStop(1.0, NiftyColor.green()));
  }

  private Set<InternalNiftyColorStop> makeColorStops(final InternalNiftyColorStop ... stops) {
    Set<InternalNiftyColorStop> result = new TreeSet<>();
    for (InternalNiftyColorStop stop : stops) {
      result.add(stop);
    }
    return result;
  }

  private void assertColorStops(final List<ColorStop> colorStops, final InternalNiftyColorStop... stops) {
    assertEquals(stops.length, colorStops.size());
    for (int i=0; i<stops.length; i++) {
      assertEquals(stops[i].getStop(), colorStops.get(i).getStop(), EPSILON);
      assertEquals(stops[i].getColor(), colorStops.get(i).getColor());
    }
  }
}
