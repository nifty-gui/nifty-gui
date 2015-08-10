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
package de.lessvoid.nifty.internal.render.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.spi.NiftyNode;
import de.lessvoid.nifty.internal.render.RenderNode;

/**
 * This class will take a list of InternalNiftyNodes and convert them into the list of RenderNodes. This will
 * traverse the whole tree including children and children children.
 *
 * @author void
 */
public class TreeSync {
  private final TreeSyncRenderNodeFactory renderNodeFactory;

  /**
   * Create a new TreeSync instance with the given Factory to create new RenderNodes.
   *
   * @param renderNodeFactory the factory to create new RenderNodes.
   */
  public TreeSync(final TreeSyncRenderNodeFactory renderNodeFactory) {
    this.renderNodeFactory = renderNodeFactory;
  }

  /**
   * Take the list of srcNodes and translate them into the list of RenderNodes.
   *
   * @param srcNodes the source list of InternalNiftyNodes
   * @param dstNodes the destination list of RenderNodes
   * @return
   */
  public boolean synchronizeTree(final List<NiftyNode> srcNodes, final List<RenderNode> dstNodes) {
    Map<Integer, RenderNode> renderNodeLookup = buildRenderNodeLookup(dstNodes);
    return syncNodes(srcNodes, dstNodes, renderNodeLookup);
  }

  private boolean syncNodes(
      final List<NiftyNode> srcNodes,
      final List<RenderNode> dstNodes,
      final Map<Integer, RenderNode> renderNodeLookup) {
    boolean result = false;
    // FIXME
    /*
    for (int i=0; i<srcNodes.size(); i++) {
      NiftyNode niftyNode = srcNodes.get(i);
      RenderNode renderNode = renderNodeLookup.get(niftyNode.getId().hashCode());

      if (hasInvalidDimension(niftyNode)) {
        if (renderNode != null) {
          dstNodes.remove(renderNode);
          result = true;
        }
        continue;
      }

      if (renderNode == null) {
        renderNode = renderNodeFactory.createRenderNode(niftyNode);
        dstNodes.add(renderNode);
        result = true;
      }

      boolean childNodeChanged = syncNodes(niftyNode.getChildren(), renderNode.getChildren(), renderNodeLookup);
      result |= childNodeChanged;
    }

    // at this point srcNodes and dstNodes contain the same elements. so here we tell all the RenderNode elements
    // their position in the list which will later be used as the sort criteria when two RenderNodes have the same
    // renderNode.
    for (int i=0; i<dstNodes.size(); i++) {
      dstNodes.get(i).setIndexInParent(i);
    }
*/
    return result;
  }
/* FIXME
  private boolean hasInvalidDimension(InternalNiftyNode niftyNode) {
    return niftyNode.getWidth() == 0 || niftyNode.getHeight() == 0;
  }
  */

  private Map<Integer, RenderNode> buildRenderNodeLookup(final List<RenderNode> dstNodes) {
    Map<Integer, RenderNode> result = new HashMap<Integer, RenderNode>();
    buildRenderNodeLookupInternal(result, dstNodes);
    return result;
  }

  private void buildRenderNodeLookupInternal(final Map<Integer, RenderNode> lookup, final List<RenderNode> nodes) {
    for (int i=0; i<nodes.size(); i++) {
      RenderNode node = nodes.get(i);
      lookup.put(node.getNodeId(), node);
      buildRenderNodeLookupInternal(lookup, node.getChildren());
    }
  }
}
