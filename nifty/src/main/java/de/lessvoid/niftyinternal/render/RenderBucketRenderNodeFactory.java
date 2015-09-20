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
package de.lessvoid.niftyinternal.render;

import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.node.NiftyNodeContentImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by void on 19.09.15.
 */
public class RenderBucketRenderNodeFactory {
  private Map<NiftyNodeContentImpl, RenderBucketRenderNode> registry = new HashMap<>();

  public RenderBucketRenderNode create(final NiftyNodeContentImpl node, final NiftyRenderDevice renderDevice) {
    RenderBucketRenderNode renderNode = registry.get(node);
    if (renderNode != null) {
      return renderNode;
    }
    renderNode = createRenderNode(node, renderDevice);
    registry.put(node, renderNode);
    return renderNode;
  }

  private RenderBucketRenderNode createRenderNode(
      final NiftyNodeContentImpl node,
      final NiftyRenderDevice renderDevice) {
    return new RenderBucketRenderNode(
        node.getContentWidth(),
        node.getContentHeight(),
        node.getLocalToScreen(),
        renderDevice);
  }
}
