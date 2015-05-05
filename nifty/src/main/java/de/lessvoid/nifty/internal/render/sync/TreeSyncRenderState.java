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

import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.render.RenderNode;

public class TreeSyncRenderState {
  private final int NEEDS_RENDER = 0x01;
  private final int NEEDS_CANVAS_UPDATE = 0x02;

  /**
   * Take the list of srcNodes and dstNodes and update states to reflect any changes.
   *
   * @param srcNodes the source list of InternalNiftyNodes
   * @param dstNodes the destination list of RenderNodes
   * @return
   */
  public int synchronize(final List<InternalNiftyNode> srcNodes, final List<RenderNode> dstNodes) {
    Map<Integer, InternalNiftyNode> niftyNodeLookup = buildNiftyNodeLookup(srcNodes);

    int result = 0;
    for (int i=0; i<dstNodes.size(); i++) {
      result |= syncNodes(dstNodes.get(i), niftyNodeLookup);
    }
    return result;
  }

  /**
   * This method takes the dst node and synchronizes the renderstate with the corresponding InternalNiftyNode.
   * It will check for changes in the canvas and if it detects some the dst node will be marked to be updated.
   * Additionally this method will check if a nodes width/height or transformation has changed and if so they
   * are marked to be rendered.
   *
   * This method will further process and repeat the same check for its child elements.
   *
   * @param dst the RenderNode (destination)
   * @param niftyNodeLookup a map to lookup InternalNiftyNode by id
   * @return a combination of NEEDS_RENDER and NEEDS_CANVAS_UPDATE or 0 if nothing has changed
   */
  private int syncNodes(final RenderNode dst, final Map<Integer, InternalNiftyNode> niftyNodeLookup) {
    InternalNiftyNode src = niftyNodeLookup.get(dst.getNodeId());
    boolean needsCanvasUpdate = needsCanvasUpdate(src, dst);
    boolean needsRender = needsRender(src, dst);

    boolean anyChildNeedsCanvasUpdate = false;
    boolean anyChildNeedsRender = false;
    for (int i=0; i<dst.getChildren().size(); i++) {
      int childChanged = syncNodes(dst.getChildren().get(i), niftyNodeLookup);
      anyChildNeedsCanvasUpdate = anyChildNeedsCanvasUpdate || ((childChanged & NEEDS_CANVAS_UPDATE) != 0);
      anyChildNeedsRender = anyChildNeedsRender || ((childChanged & NEEDS_RENDER) != 0);
    }

    int result = 0;
    if (needsCanvasUpdate || anyChildNeedsCanvasUpdate) {
      dst.needsContentUpdate(src.getCanvas().getCommands());
      result |= NEEDS_CANVAS_UPDATE;
    }

    if (needsRender || anyChildNeedsRender) {
      dst.setLocal(src.getLocalTransformation());
      dst.setWidth(src.getWidth());
      dst.setHeight(src.getHeight());
      dst.needsRender();
      result |= NEEDS_RENDER;
    }

    return result;
  }

  private boolean needsCanvasUpdate(final InternalNiftyNode src, final RenderNode dst) {
    return src.getCanvas().isChanged();
  }

  private boolean needsRender(final InternalNiftyNode src, final RenderNode dst) {
    boolean widthChanged = dst.getWidth() != src.getWidth();
    boolean heightChanged = dst.getHeight() != src.getHeight();
    boolean transformationChanged = src.isTransformationChanged();
    return (widthChanged || heightChanged || transformationChanged);
  }

  private Map<Integer, InternalNiftyNode> buildNiftyNodeLookup(final List<InternalNiftyNode> srcNodes) {
    Map<Integer, InternalNiftyNode> result = new HashMap<Integer, InternalNiftyNode>();
    buildRenderNodeLookupInternal(result, srcNodes);
    return result;
  }

  private void buildRenderNodeLookupInternal(final Map<Integer, InternalNiftyNode> lookup, final List<InternalNiftyNode> nodes) {
    for (int i=0; i<nodes.size(); i++) {
      InternalNiftyNode node = nodes.get(i);
      lookup.put(node.getId().hashCode(), node);
      buildRenderNodeLookupInternal(lookup, node.getChildren());
    }
  }

}
