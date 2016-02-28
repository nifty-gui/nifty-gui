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
package de.lessvoid.nifty.node;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLinearGradient;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by void on 28.02.16.
 */
public class NiftyBackgroundFillNodeTest {

  @Test
  public void testCreateWithColor() {
    NiftyBackgroundFillNode node = NiftyBackgroundFillNode.backgroundFillColor(NiftyColor.fuchsia());
    assertEquals(NiftyColor.fuchsia(), node.getBackgroundColor());
  }

  @Test
  public void testCreateWithGradient() {
    NiftyBackgroundFillNode node = NiftyBackgroundFillNode.backgroundFillGradient(NiftyLinearGradient.linearGradientFromAngleInDeg(0));
    assertNull(node.getBackgroundColor());
    assertEquals(NiftyLinearGradient.linearGradientFromAngleInDeg(0), node.getBackgroundGradient());
  }

  @Test
  public void testChangeBackgroundColor() {
    NiftyBackgroundFillNode node = NiftyBackgroundFillNode.backgroundFillColor(NiftyColor.fuchsia());
    node.setBackgroundColor(NiftyColor.aqua());
    assertEquals(NiftyColor.aqua(), node.getBackgroundColor());
  }

  @Test
  public void testChangeBackgroundGradient() {
    NiftyBackgroundFillNode node = NiftyBackgroundFillNode.backgroundFillGradient(NiftyLinearGradient.linearGradientFromAngleInDeg(0));
    node.setBackgroundGradient(NiftyLinearGradient.linearGradientFromAngleInDeg(90));
    assertEquals(NiftyLinearGradient.linearGradientFromAngleInDeg(90), node.getBackgroundGradient());
  }

  @Test
  public void testToStringBackgroundColorOnly() {
    NiftyBackgroundFillNode node = NiftyBackgroundFillNode.backgroundFillColor(NiftyColor.fuchsia());
    assertEquals(
      "(NiftyBackgroundFillNode) " +
        "backgroundGradient [null] " +
        "backgroundColor [#ff00ffff {1.0, 0.0, 1.0, 1.0}]", node.toString());
  }

  @Test
  public void testToStringBackgroundGradientOnly() {
    NiftyBackgroundFillNode node = NiftyBackgroundFillNode.backgroundFillGradient(NiftyLinearGradient.linearGradientFromAngleInDeg(0));
    assertEquals(
      "(NiftyBackgroundFillNode) " +
        "backgroundGradient [NiftyLinearGradient(startX[0.0] startY[0.0] endX[0.0] endY[0.0] angleInRadiants[0.0] angleMode[true] colorStops [])] " +
        "backgroundColor [null]", node.toString());
  }
}
