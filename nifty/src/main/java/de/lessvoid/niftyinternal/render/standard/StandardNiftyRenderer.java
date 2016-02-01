/*
 * Copyright (c) 2016, Nifty GUI Community
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
package de.lessvoid.niftyinternal.render.standard;

import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeContentImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeStateImpl;
import de.lessvoid.nifty.NiftyConfiguration;
import de.lessvoid.niftyinternal.accessor.NiftyStateAccessor;
import de.lessvoid.niftyinternal.common.Statistics;
import de.lessvoid.niftyinternal.render.*;
import de.lessvoid.niftyinternal.render.batch.BatchManager;
import de.lessvoid.niftyinternal.tree.InternalNiftyTree;

import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeControls.downToFirstInstance;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.toNodeImplClass;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplClass;

/**
 * Created by void on 16.01.16.
 */
public class StandardNiftyRenderer implements NiftyRenderer {
  private final BatchManager batchManager = new BatchManager();
  private final RenderBucketRenderNodeFactory renderNodeFactory;

  private final Statistics stats;
  private final NiftyRenderDevice renderDevice;
  private final RenderBucketManager renderBucketManager;

  private InternalNiftyTree tree;

  public StandardNiftyRenderer(
      final Statistics stats,
      final NiftyRenderDevice renderDevice,
      final NiftyConfiguration niftyConfiguration) {
    this.stats = stats;
    this.renderNodeFactory = new RenderBucketRenderNodeFactory(stats);
    this.renderDevice = renderDevice;
    this.renderBucketManager = new RenderBucketManager(renderDevice, niftyConfiguration, stats);
  }

  @Override
  public boolean render(final InternalNiftyTree tree) {
    this.tree = tree;

    nodeStatePass();
    nodeContentPass();
    renderBuckets();

    return true;
  }

  private void nodeStatePass() {
    stats.startRenderStatePass();
    nodeStatePass(tree.getRootNode(), newNiftyState());
    stats.stopRenderStatePass();
  }

  private void nodeContentPass() {
    stats.startRenderContentPass();
    renderBucketManager.begin();
    nodeContentPass(tree.getRootNode());
    stats.stopRenderContentPass();
  }

  private void renderBuckets() {
    stats.startRenderPass();
    renderDevice.beginRender();

    batchManager.begin();
    renderBucketManager.render(batchManager, renderDevice);
    stats.incBatchCount(batchManager.end(renderDevice));

    renderDevice.endRender();
    stats.stopRenderPass();
  }

  private NiftyState newNiftyState() {
    return NiftyStateAccessor.getDefault().newNiftyState();
  }

  private void nodeStatePass(final NiftyNodeImpl<? extends NiftyNode> startNode, final NiftyState thisLevelState) {
    for (NiftyNodeStateImpl child : getNiftyNodeStates(startNode)) {
      NiftyState saveState = NiftyStateAccessor.getDefault().copyNiftyState(thisLevelState);
      child.update(saveState);
      nodeStatePass(child, saveState);
    }
  }

  private void nodeContentPass(final NiftyNodeImpl<? extends NiftyNode> parentNode) {
    for (NiftyNodeContentImpl child : getNiftyNodeContent(parentNode)) {
      RenderBucketRenderNode renderNode = renderNodeFactory.create(child, renderDevice);
      renderNode.updateCanvas(child, renderDevice);
      renderBucketManager.update(renderNode);
      nodeContentPass(child);
    }
  }

  private Iterable<NiftyNodeStateImpl> getNiftyNodeStates(final NiftyNodeImpl<? extends NiftyNode> startNode) {
    return tree.childNodes(
          nodeImplClass(NiftyNodeStateImpl.class),
          toNodeImplClass(NiftyNodeStateImpl.class),
          downToFirstInstance(NiftyNodeStateImpl.class),
          startNode);
  }

  private Iterable<NiftyNodeContentImpl> getNiftyNodeContent(final NiftyNodeImpl<? extends NiftyNode> startNode) {
    return tree.childNodes(
          nodeImplClass(NiftyNodeContentImpl.class),
          toNodeImplClass(NiftyNodeContentImpl.class),
          downToFirstInstance(NiftyNodeContentImpl.class),
          startNode);
  }

}
