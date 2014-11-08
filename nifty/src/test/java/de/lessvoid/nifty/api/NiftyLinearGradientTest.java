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
package de.lessvoid.nifty.api;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class NiftyLinearGradientTest {

  @Test
  public void testPoints() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    assertEquals(0.0, gradient.getX0());
    assertEquals(1.0, gradient.getY0());
    assertEquals(170.0, gradient.getX1());
    assertEquals(2.0, gradient.getY1());
  }

  @Test
  public void testSingleColor() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.BLACK()));
  }

  @Test
  public void testTwoColors() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    gradient.addColorStop(1.0, NiftyColor.GREEN());
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.BLACK()),
        new NiftyColorStop(1.0, NiftyColor.GREEN()));
  }

  @Test
  public void testThreeColors() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    gradient.addColorStop(0.5, NiftyColor.RED());
    gradient.addColorStop(1.0, NiftyColor.GREEN());
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.BLACK()),
        new NiftyColorStop(0.5, NiftyColor.RED()),
        new NiftyColorStop(1.0, NiftyColor.GREEN()));
  }

  @Test
  public void testReplaceColor() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    gradient.addColorStop(0.0, NiftyColor.BLUE());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.BLUE()));
  }

  private void assertColorStops(final List<NiftyColorStop> colorStops, final NiftyColorStop ... stops) {
    assertEquals(colorStops.size(), stops.length);
    for (int i=0; i<stops.length; i++) {
      assertTrue(colorStops.get(i).equals(stops[i]));
      assertTrue(colorStops.get(i).getColor().equals(stops[i].getColor()));
    }
  }
}
