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

/**
 * The NiftyConfiguration class stores all the parameters a user might want to tweak when using Nifty.
 * <br/>
 * Created by void on 13.09.15.
 */
public final class NiftyConfiguration {
  private boolean clearScreen;
  private int renderBucketWidth = 256;
  private int renderBucketHeight = 256;
  private boolean showRenderBuckets = false;
  private boolean showRenderNodes = false;
  private NiftyColor showRenderNodesOverlayColor = NiftyColor.fromString("#ff00ff50");

  /**
   * Call this to let Nifty clear the screen before it renders the GUI. This might be useful when the only thing you're
   * currently rendering is the GUI. If you render the GUI as an overlay you better not enable that :)
   * <br/>
   * The default value is false.
   * <br/>
   * @param clearScreen set this to true to automatically clear the screen
   * @return this
   */
  public NiftyConfiguration clearScreen(final boolean clearScreen) {
    this.clearScreen = clearScreen;
    return this;
  }

  /**
   * Get the current state of the clearScreen setting.
   * <br/>
   * @return the clearScreen setting
   */
  public boolean isClearScreen() {
    return clearScreen;
  }

  /**
   * Set the render bucket width.
   * <br/>
   * The default value is 256.
   * <br/>
   * @param renderBucketWidth the new width of the render buckets to use
   * @return this
   */
  public NiftyConfiguration renderBucketWidth(final int renderBucketWidth) {
    this.renderBucketWidth = renderBucketWidth;
    return this;
  }

  /**
   * Get the current render bucket width.
   * <br/>
   * @return the current render bucket width
   */
  public int getRenderBucketWidth() {
    return renderBucketWidth;
  }

  /**
   * Set the render bucket height.
   * <br/>
   * The default value is 256.
   * <br/>
   * @param renderBucketHeight the new height of the render buckets to use
   * @return this
   */
  public NiftyConfiguration renderBucketHeight(final int renderBucketHeight) {
    this.renderBucketHeight = renderBucketHeight;
    return this;
  }

  /**
   * Get the current render bucket height.
   * <br/>
   * @return the current render bucket height
   */
  public int getRenderBucketHeight() {
    return renderBucketHeight;
  }

  /**
   * Set showRenderBuckets to true to overlay a random color quad above each updated RenderBucket.
   * <br/>
   * @param renderShowBuckets the value to set showRenderBuckets to
   * @return this
   */
  public NiftyConfiguration showRenderBuckets(final boolean renderShowBuckets) {
    this.showRenderBuckets = renderShowBuckets;
    return this;
  }

  /**
   * Get the current showRenderBuckets value.
   * <br/>
   * @return the current showRenderBuckets value
   */
  public boolean isShowRenderBuckets() {
    return showRenderBuckets;
  }

  /**
   * Set showRenderNodes to true to overlay a quad in the color set to the showRenderNodesOverlayColor.
   * <br/>
   * @param showRenderNodes the value to set showRenderNodes to
   * @return this
   */
  public NiftyConfiguration showRenderNodes(final boolean showRenderNodes) {
    this.showRenderNodes = showRenderNodes;
    return this;
  }

  /**
   * Get the current value of the showRenderNodes parameter.
   * <br/>
   * @return the current value of the showRenderNodes parameter
   */
  public boolean isShowRenderNodes() {
    return showRenderNodes;
  }

  /**
   * Set the showRenderNodesOverlayColor - the color used to overlay
   * each renderNode when showRenderNodes is set to true.
   * <br/>
   * @param showRenderNodesOverlayColor the color to render showRenderNodes overlays with
   * @return this
   */
  public NiftyConfiguration renderShowNodesOverlayColor(final NiftyColor showRenderNodesOverlayColor) {
    this.showRenderNodesOverlayColor = showRenderNodesOverlayColor;
    return this;
  }

  /**
   * Get the current showRenderNodesOverlayColor.
   * <br/>
   * @return the showRenderNodesOverlayColor
   */
  public NiftyColor getShowRenderNodesOverlayColor() {
    return showRenderNodesOverlayColor;
  }
}
