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
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.node.NiftyNodeContentImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeStateImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;
import de.lessvoid.niftyinternal.accessor.NiftyStateAccessor;
import de.lessvoid.niftyinternal.common.Statistics;
import de.lessvoid.niftyinternal.render.batch.BatchManager;
import de.lessvoid.niftyinternal.tree.InternalNiftyTree;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.toNodeImplClass;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplClass;

/**
 * Take a list of NiftyNodeImpl and render them.
 *
 * @author void
 */
public class InternalNiftyRenderer {
  private final static Logger logger = Logger.getLogger(InternalNiftyRenderer.class.getName());

  private final RenderBucketRenderNodeFactory renderNodeFactory = new RenderBucketRenderNodeFactory();
  private final Statistics stats;
  private final NiftyRenderDevice renderDevice;

  // we keep all RenderNodeCanvas instances sorted in a list of buckets
  // a bucket is a part of the screen that is cached in a texture. when rendering the screen we only
  // re-render buckets that contain changed RenderNodeCanvas instances.
  // a RenderNodeCanvas can be a part of multiple buckets. When a RenderNodeCanvas overlaps multiple
  // buckets in is stored in all that it overlaps.
  private final List<RenderBucket> renderBucketList;

  public InternalNiftyRenderer(
      final Statistics stats,
      final NiftyRenderDevice renderDevice,
      final RenderBucketConfiguration config) {
    this.stats = stats;
    this.renderDevice = renderDevice;
    this.renderBucketList = createBuckets(renderDevice.getDisplayWidth(), renderDevice.getDisplayHeight(), config);
  }

  public boolean render(final InternalNiftyTree tree) {
    nodeStatePass(tree.childNodes(nodeImplClass(NiftyNodeStateImpl.class), toNodeImplClass(NiftyNodeStateImpl.class)));
    nodeCanvasPass(tree.childNodes(nodeImplClass(NiftyNodeContentImpl.class), toNodeImplClass(NiftyNodeContentImpl.class)));
    render();
    return true;
  }

  private void nodeStatePass(final Iterable<NiftyNodeStateImpl> nodes) {
    NiftyState niftyState = NiftyStateAccessor.getDefault().newNiftyState();
    for (NiftyNodeStateImpl child : nodes) {
      child.update(niftyState);
    }
  }

  private void nodeCanvasPass(final Iterable<NiftyNodeContentImpl> nodes) {
    for (NiftyNodeContentImpl child : nodes) {
      RenderBucketRenderNode renderNode = renderNodeFactory.create(child, renderDevice);
      renderNode.updateCanvas(child);
      renderNode.updateContent(child.getContentWidth(), child.getContentHeight(), child.getScreenToLocal(), renderDevice);
      updateRenderBuckets(renderNode);
    }
  }

  private void updateRenderBuckets(final RenderBucketRenderNode renderNode) {
    for (RenderBucket renderBucket : renderBucketList) {
      renderBucket.update(renderNode);
    }
  }

  private void render() {
    renderDevice.beginRender();

    BatchManager batchManager = new BatchManager();
    batchManager.begin();
    for (RenderBucket renderBucket : renderBucketList) {
      renderBucket.render(batchManager, renderDevice);
    }
    batchManager.end(renderDevice);

    renderDevice.endRender();
  }

  private List<RenderBucket> createBuckets(
      final int displayWidth,
      final int displayHeight,
      final RenderBucketConfiguration config) {
    List<RenderBucket> result = new ArrayList<>();
    for (int y=0; y<displayHeight/config.getBucketHeight(); y++) {
      for (int x=0; x<displayWidth/config.getBucketWidth(); x++) {
        result.add(
            new RenderBucket(
                NiftyRect.newNiftyRect(
                    NiftyPoint.newNiftyPoint(x * config.getBucketWidth(), y * config.getBucketHeight()),
                    NiftySize.newNiftySize(config.getBucketWidth(), config.getBucketHeight())
                ),
                renderDevice));
      }
    }
    return result;
  }
}
