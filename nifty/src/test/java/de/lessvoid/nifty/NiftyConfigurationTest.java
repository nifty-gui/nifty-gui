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
package de.lessvoid.nifty;

import de.lessvoid.nifty.types.NiftyColor;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by void on 13.09.15.
 */
public class NiftyConfigurationTest {
  private final NiftyConfiguration config = new NiftyConfiguration();

  @Test
  public void testDefaults() {
    assertEquals(256, config.getRenderBucketWidth());
    assertEquals(256, config.getRenderBucketHeight());
    assertFalse(config.isShowRenderBuckets());
    assertFalse(config.isShowRenderNodes());
    assertNotNull(config.getShowRenderNodesOverlayColor());
  }

  @Test
  public void testClearScreen() {
    config.clearScreen(true);
    assertTrue(config.isClearScreen());
  }

  @Test
  public void testSetRenderBucketWidth() {
    config.renderBucketWidth(100);
    assertEquals(100, config.getRenderBucketWidth());
  }

  @Test
  public void testSetRenderBucketHeight() {
    config.renderBucketHeight(100);
    assertEquals(100, config.getRenderBucketHeight());
  }

  @Test
  public void testEnableShowRenderNodeOverlay() {
    config.showRenderNodes(true);
    assertTrue(config.isShowRenderNodes());
    assertNotNull(config.getShowRenderNodesOverlayColor());
  }

  @Test
  public void testEnableShowRenderNodeOverlayWithColor() {
    config.showRenderNodes(true);
    config.renderShowNodesOverlayColor(NiftyColor.aqua());
    assertTrue(config.isShowRenderNodes());
    assertEquals(NiftyColor.aqua(), config.getShowRenderNodesOverlayColor());
  }

  @Test
  public void testEnableShowRenderBuckets() {
    config.showRenderBuckets(true);
    assertTrue(config.isShowRenderBuckets());
  }
}
