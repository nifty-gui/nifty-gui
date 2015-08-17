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
package de.lessvoid.nifty.spi;

import de.lessvoid.niftyinternal.render.RenderNode;

/**
 * NiftyNodeRenderImpl is a renderable NiftyNode. It's task is to create or update a RenderNode instance from its
 * internal state.
 *
 * Created by void on 08.08.15.
 */
public interface NiftyNodeRenderImpl {

  /**
   * Initialize this NiftyNodeRenderImpl with a NiftyRenderDevice.
   * @param niftyRenderDevice the NiftyRenderDevice
   */
  void initialize(NiftyRenderDevice niftyRenderDevice);

  /**
   * Convert the state of the implementing class into a RenderNode. If at a previous time this Node has been associated
   * with a RenderNode the existing RenderNode linked to this node will be given as a parameter. Otherwise the parameter
   * will be null.
   *
   * @param renderNode the existing RenderNode for this NiftyNodeRenderImpl or null
   * @return the updated or the new RenderNode for this
   */
  RenderNode convert(RenderNode renderNode);
}
