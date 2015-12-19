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

/**
 * Created by void on 13.09.15.
 */
public class NiftyConfigurationImpl implements NiftyConfiguration {
  private final int bucketWidth = 256;
  private final int bucketHeight = 256;
  private boolean showRenderNodeOverlayEnabled = false;
  private NiftyColor showRenderNodeOverlayColor = NiftyColor.fromString("#ff00ff50");

  @Override
  public int getBucketWidth() {
    return bucketWidth;
  }

  @Override
  public int getBucketHeight() {
    return bucketHeight;
  }

  @Override
  public boolean showRenderNodeOverlay() {
    return showRenderNodeOverlayEnabled;
  }

  @Override
  public NiftyColor showRenderNodeOverlayColor() {
    return showRenderNodeOverlayColor;
  }

  public void enableShowRenderNodeOverlay() {
    this.showRenderNodeOverlayEnabled = true;
  }

  public void enableShowRenderNodeOverlay(final NiftyColor color) {
    this.showRenderNodeOverlayEnabled = true;
    this.showRenderNodeOverlayColor = color;
  }

  public void disableShowRenderNodeOverlay() {
    this.showRenderNodeOverlayEnabled = false;
  }
}
