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
package de.lessvoid.nifty.internal.render;

import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice.FilterMode;

/**
 * Synchronize a list of NiftyNodes to a list of RootRenderNode.
 *
 * @author void
 */
public class RendererNodeSync {
  // since we want to access the nodes private API we need a NiftyNodeAccessor
  private final NiftyNodeAccessor niftyNodeAccessor;

  // and since we need to create render resources like textures we'll keep an NiftyRenderDevice around as well
  private final NiftyRenderDevice renderDevice;

  /**
   * Create the RendererNodeSync using the NiftyRenderDevice given.
   * @param renderDevice the NiftyRenderDevice
   */
  public RendererNodeSync(final NiftyRenderDevice renderDevice) {
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  /**
   * Synchronize the list of given NiftyNode srcNodes into the list of RootRenderNode in dstNodes.
   * @param srcNodes the source nodes
   * @param dstNodes the destination nodes
   * @return true if anything changed and false if it is exactly the same as in the last frame
   */
  public boolean synchronize(final List<NiftyNode> srcNodes, final List<RenderNode> dstNodes) {
    boolean changed = false;

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = niftyNodeAccessor.getInternalNiftyNode(srcNodes.get(i));

      // skip nodes that have zero size
      if (src.getWidth() == 0 && src.getHeight() == 0) {
        continue;
      }

      RenderNode dst = findNode(dstNodes, src.getId());
      if (dst == null) {
        RenderNode childRenderNode = createRenderNode(src);
        dstNodes.add(childRenderNode);
        childRenderNode.setIndexInParent(dstNodes.size() - 1);

        changed = true;
        continue;
      }
      boolean syncChanged = syncRenderNodeBufferChildNodes(src, dst) != 0;
      changed = changed || syncChanged;
    }

    return changed;
  }

  private RenderNode findNode(final List<RenderNode> nodes, final int id) {
    for (int i=0; i<nodes.size(); i++) {
      RenderNode node = nodes.get(i);
      if (node.getNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  private RenderNode createRenderNode(final InternalNiftyNode node) {
    RenderNode renderNode = new RenderNode(
        node.getId(),
        node.getLocalTransformation(),
        node.getWidth(),
        node.getHeight(),
        node.getCanvas().getCommands(),
        renderDevice.createTexture(node.getWidth(), node.getHeight(), FilterMode.Linear),
        renderDevice.createTexture(node.getWidth(), node.getHeight(), FilterMode.Linear),
        node.getCompositeOperation(),
        node.getRenderOrder());

    for (int i=0; i<node.getChildren().size(); i++) {
      RenderNode childRenderNode = createRenderNode(node.getChildren().get(i));
      renderNode.addChildNode(childRenderNode);
      childRenderNode.setIndexInParent(i);
    }

    return renderNode;
  }

  private final int NEEDS_RENDER = 0x01;
  private final int NEEDS_CONTENT = 0x02;

  private int syncRenderNodeBufferChildNodes(final InternalNiftyNode src, final RenderNode dst) {
    boolean canvasChanged = canvasChanged(src, dst);
    boolean needsRender = needsRender(src, dst);

    boolean childCanvasChanged = false;
    boolean childNeedsRender = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      int currentChildChanged = syncRenderNodeBufferChild(src.getChildren().get(i), dst);
      childCanvasChanged = childCanvasChanged || ((currentChildChanged & NEEDS_CONTENT) != 0);
      childNeedsRender = childNeedsRender || ((currentChildChanged & NEEDS_RENDER) != 0);
    }

    int result = 0;
    if (canvasChanged || childCanvasChanged) {
      dst.needsContentUpdate(src.getCanvas().getCommands());
      result |= NEEDS_CONTENT;
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

  private boolean canvasChanged(final InternalNiftyNode src, final RenderNode dst) {
    if (src.getCanvas().isChanged()) {
      return true;
    }

    boolean widthChanged = dst.getWidth() != src.getWidth();
    boolean heightChanged = dst.getHeight() != src.getHeight();
    if (widthChanged || heightChanged) {
//      if (RedrawOnSizeChange) {
//        return true;
//      }
    }
    return false;
  }

  private boolean needsRender(final InternalNiftyNode src, final RenderNode dst) {
    boolean widthChanged = dst.getWidth() != src.getWidth();
    boolean heightChanged = dst.getHeight() != src.getHeight();
    boolean transformationChanged = src.isTransformationChanged();
    return (widthChanged || heightChanged || transformationChanged);
  }

  private int syncRenderNodeBufferChild(final InternalNiftyNode src, final RenderNode dstParent) {
    RenderNode dst = dstParent.findChildWithId(src.getId());
    if (dst == null) {
      dstParent.addChildNode(createRenderNode(src));
      return NEEDS_RENDER | NEEDS_CONTENT;
    }
    return syncRenderNodeBufferChildNodes(src, dst);
  }
}
