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

import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.spi.NiftyNodeRenderImpl;
import de.lessvoid.nifty.spi.NiftyNodeStateImpl;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.niftyinternal.common.Statistics;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;
import de.lessvoid.niftyinternal.tree.InternalNiftyTree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Take a list of NiftyNodeImpl and render them.
 *
 * @author void
 */
public class InternalNiftyRenderer {
  private final static Logger logger = Logger.getLogger(InternalNiftyRenderer.class.getName());

  private final Statistics stats;
  private final NiftyRenderDevice renderDevice;
  private final Map<Integer, RenderNode> existingNodes = new LinkedHashMap<>();

  public InternalNiftyRenderer(final Statistics stats, final NiftyRenderDevice renderDevice) {
    this.stats = stats;
    this.renderDevice = renderDevice;
  }

  public boolean render(final InternalNiftyTree tree) {
    nodeStatePass(tree.filteredChildNodesGeneral(NiftyNodeStateImpl.class));
    nodeRenderPass(tree.filteredChildNodesGeneral(NiftyNodeRenderImpl.class));
    render();
    return true;
  }

  private void nodeStatePass(final Iterable<NiftyNodeStateImpl> nodes) {
    NiftyState niftyState = new NiftyState();
    for (NiftyNodeStateImpl child : nodes) {
      child.update(niftyState);
    }
  }

  private void nodeRenderPass(final Iterable<NiftyNodeRenderImpl> nodes) {
    for (NiftyNodeRenderImpl child : nodes) {
      existingNodes.put(child.hashCode(), child.convert(existingNodes.get(child.hashCode())));
    }
  }

  private void render() {
    renderDevice.beginRender();

    BatchManager batchManager = new BatchManager();
    batchManager.begin();
    for (RenderNode renderNode : existingNodes.values()) {
      renderNode.render(batchManager, renderDevice);
    }
    batchManager.end(renderDevice);

    renderDevice.endRender();
  }
}
