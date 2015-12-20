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
package de.lessvoid.niftyinternal;

import de.lessvoid.nifty.types.NiftyColor;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by void on 13.09.15.
 */
public class NiftyConfigurationImplTest {

  @Test
  public void testDefaults() {
    NiftyConfigurationImpl config = new NiftyConfigurationImpl();
    assertEquals(256, config.getBucketWidth());
    assertEquals(256, config.getBucketHeight());
    assertFalse(config.isShowRenderBuckets());
    assertFalse(config.isShowRenderNodes());
    assertNotNull(config.getShowRenderNodeOverlayColor());
  }

  @Test
  public void testEnableShowRenderNodeOverlay() {
    NiftyConfigurationImpl config = new NiftyConfigurationImpl();
    config.setShowRenderNodes(true);
    assertTrue(config.isShowRenderNodes());
    assertNotNull(config.getShowRenderNodeOverlayColor());
  }

  @Test
  public void testEnableShowRenderNodeOverlayWithColor() {
    NiftyConfigurationImpl config = new NiftyConfigurationImpl();
    config.setShowRenderNodes(true);
    config.setShowRenderNodeOverlayColor(NiftyColor.aqua());
    assertTrue(config.isShowRenderNodes());
    assertEquals(NiftyColor.aqua(), config.getShowRenderNodeOverlayColor());
  }

  @Test
  public void testEnableShowRenderBuckets() {
    NiftyConfigurationImpl config = new NiftyConfigurationImpl();
    config.setShowRenderBuckets(true);
    assertTrue(config.isShowRenderBuckets());
  }
}
