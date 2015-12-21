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
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.niftyinternal.NiftyConfiguration;
import de.lessvoid.niftyinternal.canvas.Context;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A RenderBucket is a rectangular area of the screen. A RenderBucket keeps a list
 * of RenderBucketRenderable which overlaps this RenderBucket.
 *
 * Its actual content is stored in a texture. Re-rendering of all RenderBucketRenderable
 * will only performed if any of the stored RenderBucketRenderable changed. A change
 * can be a change of its content or any of its transformation properties.
 *
 * All RenderBuckets have the same size.
 *
 * Created by void on 13.09.15.
 */
public class RenderBucket {
  private final List<RenderBucketRenderNode> renderNodes = new ArrayList<>();
  private final NiftyRect rect;
  private final Context context;
  private final Mat4 bucketTransform;
  private final Mat4 bucketTransformInverse;
  private final NiftyConfiguration config;
  private final NiftyColor debugOverlayColor = NiftyColor.randomColorWithAlpha(0.5);
  private boolean used;

  public RenderBucket(
      final NiftyRect rect,
      final NiftyRenderDevice renderDevice,
      final NiftyConfiguration config) {
    this.rect = rect;
    this.context = createContext(renderDevice);
    this.bucketTransform = Mat4.createTranslate(rect.getOrigin().getX(), rect.getOrigin().getY(), 0.f);
    this.bucketTransformInverse = Mat4.invert(bucketTransform, null);
    this.config = config;

    // update
    context.bind(renderDevice, new BatchManager());
    context.prepare();
    context.setFillColor(NiftyColor.transparent());
    context.filledRect(0., 0., rect.getSize().getWidth(), rect.getSize().getHeight());
    context.flush();

    this.used = false;
  }

  public void update(final RenderBucketRenderNode renderNode) {
    if (renderNode.getScreenSpaceAABB().isOverlapping(rect)) {
      if (!renderNodes.contains(renderNode)) {
        renderNodes.add(renderNode);
      }
    } else {
      renderNodes.remove(renderNode);
    }
  }

  public void render(final BatchManager batchManager, final NiftyRenderDevice renderDevice) {
    Collections.sort(renderNodes);

    if (!renderNodes.isEmpty()) {
      BatchManager localBatchManager = new BatchManager();
      context.bind(renderDevice, localBatchManager);
      context.prepare();

      for (int i=0; i<renderNodes.size(); i++) {
        RenderBucketRenderNode renderNode = renderNodes.get(i);
        renderNode.render(localBatchManager, bucketTransformInverse);

        if (config.isShowRenderNodes()) {
          NiftyRect r = renderNode.getScreenSpaceAABB();
          localBatchManager.addColorQuad(
              r.getOrigin().getX(),
              r.getOrigin().getY(),
              r.getOrigin().getX() + r.getSize().getWidth(),
              r.getOrigin().getY() + r.getSize().getHeight(),
              config.getShowRenderNodeOverlayColor(),
              bucketTransformInverse);
        }
      }

      if (config.isShowRenderBuckets()) {
        context.setFillColor(debugOverlayColor);
        context.filledRect(0., 0., rect.getSize().getWidth(), rect.getSize().getHeight());
      }

      context.flush();
      used = true;
    } else {
      if (used) {
        BatchManager localBatchManager = new BatchManager();
        context.bind(renderDevice, localBatchManager);
        context.prepare();
        context.setFillColor(NiftyColor.transparent());
        context.filledRect(0., 0., rect.getSize().getWidth(), rect.getSize().getHeight());
        context.flush();
        used = false;
      }
    }

    // render
    batchManager.addChangeCompositeOperation(NiftyCompositeOperation.SourceOver);
    batchManager.addTextureQuad(context.getNiftyTexture(), bucketTransform, NiftyColor.white());
  }

  private Context createContext(final NiftyRenderDevice renderDevice) {
    return new Context(
        renderDevice.createTexture(
            Math.round(rect.getSize().getWidth()),
            Math.round(rect.getSize().getHeight()),
            NiftyRenderDevice.FilterMode.Linear),
        renderDevice.createTexture(
            Math.round(rect.getSize().getWidth()),
            Math.round(rect.getSize().getHeight()),
            NiftyRenderDevice.FilterMode.Linear));
  }
}
