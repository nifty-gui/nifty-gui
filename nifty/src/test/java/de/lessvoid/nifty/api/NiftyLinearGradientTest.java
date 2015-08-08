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
package de.lessvoid.nifty.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import de.lessvoid.nifty.api.types.NiftyColor;
import de.lessvoid.nifty.api.types.NiftyColorStop;
import de.lessvoid.nifty.api.types.NiftyLinearGradient;
import org.junit.Test;

public class NiftyLinearGradientTest {

  @Test
  public void testCopyConstructor() {
    NiftyLinearGradient source = createNiftyLinearGradient();
    source.addColorStop(0.0, NiftyColor.black());
    source.addColorStop(0.0, NiftyColor.blue());
    source.setFlip();
    source.setScale(2.);

    NiftyLinearGradient gradient = new NiftyLinearGradient(source);
    assertEquals(Math.PI/2, gradient.getAngleInRadiants());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.blue()));
    assertTrue(gradient.isFlip());
    assertEquals(2., gradient.getScale());
  }

  @Test
  public void testPoints() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    assertEquals(Math.PI/2, gradient.getAngleInRadiants());
  }

  @Test
  public void testSingleColor() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    gradient.addColorStop(0.0, NiftyColor.black());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.black()));
  }

  @Test
  public void testTwoColors() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(1.0, NiftyColor.green());
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.black()),
        new NiftyColorStop(1.0, NiftyColor.green()));
  }

  @Test
  public void testThreeColors() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(0.5, NiftyColor.red());
    gradient.addColorStop(1.0, NiftyColor.green());
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.black()),
        new NiftyColorStop(0.5, NiftyColor.red()),
        new NiftyColorStop(1.0, NiftyColor.green()));
  }

  @Test
  public void testReplaceColor() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    gradient.addColorStop(0.0, NiftyColor.black());
    gradient.addColorStop(0.0, NiftyColor.blue());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.blue()));
  }

  @Test
  public void testScale() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    assertEquals(gradient, gradient.setScale(1.23));
    assertEquals(1.23, gradient.getScale());
  }

  @Test
  public void testFlipDefault() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    assertFalse(gradient.isFlip());
  }

  @Test
  public void testFlip() {
    NiftyLinearGradient gradient = createNiftyLinearGradient();
    assertEquals(gradient, gradient.setFlip());
    assertTrue(gradient.isFlip());
  }

  private NiftyLinearGradient createNiftyLinearGradient() {
    return NiftyLinearGradient.createFromAngleInRad(Math.PI/2);
  }

  private void assertColorStops(final List<NiftyColorStop> colorStops, final NiftyColorStop ... stops) {
    assertEquals(colorStops.size(), stops.length);
    for (int i=0; i<stops.length; i++) {
      assertTrue(colorStops.get(i).equals(stops[i]));
      assertTrue(colorStops.get(i).getColor().equals(stops[i].getColor()));
    }
  }
}
