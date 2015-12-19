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
 * Represents configurable values inside of Nifty.
 * Created by void on 13.09.15.
 */
public interface NiftyConfiguration {

  /**
   * The render bucket width parameter.
   * @return render bucket width
   */
  int getBucketWidth();

  /**
   * The render bucket height parameter.
   * @return render bucket height
   */
  int getBucketHeight();

  /**
   * If this is set to true all render nodes will be overlayed
   * by a single colored quad to display and debug its position.
   * @return the showRenderNodeOverlay parameter
   */
  boolean showRenderNodeOverlay();

  /**
   * This returns the overlay color to be used when showRenderNodeOverlay
   * is enabled.
   * @return the color to use when showRenderNodeOverlay is enabled
   */
  NiftyColor showRenderNodeOverlayColor();
}
