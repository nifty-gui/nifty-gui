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

import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.render.RenderNode;

public class RendererNodeSyncInternal {
  private final int NEEDS_RENDER = 0x01;
  private final int NEEDS_CANVAS_UPDATE = 0x02;

  /**
   * This method takes the src node and compares it with the dst node. It will check for changes in the
   * canvas and if it detects some the dst node will be marked to be updated. Additionally this method will check
   * if a nodes width/height or transformation has changed and if so they are marked to be rendered.
   *
   * This method will further process and repeat the same check for its child elements.
   *
   * @param src the InternalNiftyNode (source)
   * @param dst the RenderNode (destination)
   * @param nodeFactory the nodeFactory in case new nodes will need to be created
   * @return a combination of NEEDS_RENDER and NEEDS_CANVAS_UPDATE or 0 if nothing has changed
   */
  int syncRenderNodeBufferChildNodes(
      final InternalNiftyNode src,
      final RenderNode dst,
      final RendererNodeSyncNodeFactory nodeFactory) {
    boolean needsCanvasUpdate = needsCanvasUpdate(src, dst);
    boolean needsRender = needsRender(src, dst);

    boolean childNeedsCanvasUpdate = false;
    boolean childNeedsRender = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      InternalNiftyNode srcChildNode = src.getChildren().get(i);

      int currentChildChanged = syncChilds(srcChildNode, dst, nodeFactory);
      childNeedsCanvasUpdate = childNeedsCanvasUpdate || ((currentChildChanged & NEEDS_CANVAS_UPDATE) != 0);
      childNeedsRender = childNeedsRender || ((currentChildChanged & NEEDS_RENDER) != 0);
    }

    int result = 0;
    if (needsCanvasUpdate || childNeedsCanvasUpdate) {
      dst.needsContentUpdate(src.getCanvas().getCommands());
      result |= NEEDS_CANVAS_UPDATE;
    }

    if (needsRender || childNeedsRender) {
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

  private int syncChilds(
      final InternalNiftyNode src,
      final RenderNode dstParent,
      final RendererNodeSyncNodeFactory nodeFactory) {
    RenderNode dst = dstParent.findChildWithId(src.getId().hashCode());
    if (dst == null) {
      dstParent.addChildNode(nodeFactory.createRenderNode(src));
      return NEEDS_RENDER | NEEDS_CANVAS_UPDATE;
    }
    return syncRenderNodeBufferChildNodes(src, dst, nodeFactory);
  }
}
