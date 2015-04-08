/*
 * Copyright (c) 2014, Jens Hohmuth 
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
package de.lessvoid.nifty.internal.render.sync;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.render.RenderNode;

/**
 * Synchronize a list of NiftyNodes to a list of RootRenderNode.
 *
 * @author void
 */
public class RenderNodeSync {
  // this class will create RenderNodes for us
  private RendererNodeSyncNodeFactory renderNodeFactory;

  // the actual process of syncing a hierarchy of renderNodes
  private RendererNodeSyncInternal renderNodeSyncInternal;

  // helper list used in synchronize() method to keep track of all nodes that still exist
  private Set<Integer> existingNodes = new HashSet<Integer>();

  // we keep a map for lookup of RenderNodes by id
  private Map<Integer, RenderNode> renderNodeLookup = new HashMap<Integer, RenderNode>();

  /**
   * Create the RendererNodeSync using the RendererNodeSyncNodeFactory and RendererNodeSyncInternal given.
   *
   * @param renderNodeFactory the RendererNodeSyncNodeFactory
   */
  public RenderNodeSync(
      final RendererNodeSyncNodeFactory renderNodeFactory,
      final RendererNodeSyncInternal renderNodeSyncInternal) {
    this.renderNodeFactory = renderNodeFactory;
    this.renderNodeSyncInternal = renderNodeSyncInternal;
  }

  /**
   * Synchronize the list of given NiftyNode srcNodes into the list of RootRenderNode in dstNodes.
   *
   * @param srcNodes the source nodes
   * @param dstNodes the destination nodes
   * @return true if anything changed and false if it is exactly the same as in the last frame
   */
  public boolean synchronize(final List<InternalNiftyNode> srcNodes, final List<RenderNode> dstNodes) {
    boolean changed = false;

    // build the RenderNode lookup
    Map<Integer, RenderNode> renderNodeLookup = buildRenderNodeLookup(dstNodes);

    // we'll start with the existingNodes list empty
    existingNodes.clear();

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = srcNodes.get(i);

      // skip nodes that have zero size
      if (src.getWidth() <= 0 || src.getHeight() <= 0) {
        continue;
      }

      // now check if we already have a RenderNode for this srcNode
      RenderNode dst = renderNodeLookup.get(src.getId().hashCode());

      if (dst != null) {
        // synchronize an existing RenderNode with the current srcNode
        boolean syncChanged = renderNodeSyncInternal.syncRenderNodeBufferChildNodes(src, dst, renderNodeFactory) != 0;
        changed = changed || syncChanged;
      } else {
        // create a new RenderNode for this src node
        // TODO is it actually correct to add the new RenderNode to the end of the List? o_O
        dst = renderNodeFactory.createRenderNode(src);
        dst.setIndexInParent(dstNodes.size());
        dstNodes.add(dst);
        changed = true;
      }

      // the dstNode exists as a RenderNode
      existingNodes.add(dst.getNodeId());
    }

    // we now need to remove all RenderNodes from dstNodes that are not part of existingNodes since
    // they have been removed in the src list
    Iterator<RenderNode> it = dstNodes.iterator();
    while (it.hasNext()) {
      RenderNode renderNode = it.next();
      if (!existingNodes.contains(renderNode.getNodeId())) {
        it.remove();
        changed = true;
      }
    }

    return changed;
  }

  private Map<Integer, RenderNode> buildRenderNodeLookup(final List<RenderNode> dstNodes) {
    renderNodeLookup.clear();
    for (int i=0; i<dstNodes.size(); i++) {
      RenderNode node = dstNodes.get(i);
      renderNodeLookup.put(node.getNodeId(), node);
    }
    return renderNodeLookup;
  }
}
