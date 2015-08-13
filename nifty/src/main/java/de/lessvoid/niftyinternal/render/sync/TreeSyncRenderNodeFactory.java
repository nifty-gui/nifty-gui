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
package de.lessvoid.niftyinternal.render.sync;

import de.lessvoid.nifty.spi.NiftyNode;
import de.lessvoid.niftyinternal.render.RenderNode;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * Factory to create new RenderNodes.
 *
 * @author void
 */
public class TreeSyncRenderNodeFactory {
  private final NiftyRenderDevice renderDevice;

  public TreeSyncRenderNodeFactory(final NiftyRenderDevice renderDevice) {
    this.renderDevice = renderDevice;
  }

  /**
   * Create a new RenderNode from the given InternalNiftyNode.
   * Please note that this will only create the given node WITHOUT any child nodes.
   *
   * @param node the source InternalNiftyNode to create a RenderNode for
   * @return the newly created RenderNode
   */
  public RenderNode createRenderNode(final NiftyNode node) {
    return null;
    /*
    return new RenderNode(
        node.getId().hashCode(),
        node.getLocalTransformation(),
        node.getWidth(),
        node.getHeight(),
        node.getCanvas().getCommands(),
        renderDevice.createTexture(node.getWidth(), node.getHeight(), FilterMode.Linear),
        renderDevice.createTexture(node.getWidth(), node.getHeight(), FilterMode.Linear),
        node.getCompositeOperation(),
        node.getRenderOrder());
        */
  }

}
