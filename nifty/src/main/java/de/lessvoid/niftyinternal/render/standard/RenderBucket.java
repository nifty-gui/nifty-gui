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
package de.lessvoid.niftyinternal.render.standard;

import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.NiftyConfiguration;
import de.lessvoid.niftyinternal.canvas.Context;
import de.lessvoid.niftyinternal.common.Statistics;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A RenderBucket is a rectangular area of the screen. A RenderBucket keeps a list of RenderBucketRenderNode which
 * overlaps this RenderBucket.
 * <p/>
 * Its actual content is stored in a texture. Re-rendering of all RenderBucketRenderNodes will only performed if any of
 * the stored RenderBucketRenderNode changed. A change can be a change of its content or any of its transformation
 * properties.
 * <p/>
 * All RenderBuckets have the same size.
 * <p/>
 * Created by void on 13.09.15.
 */
public class RenderBucket {
  private final List<RenderBucketRenderNode> renderNodes = new ArrayList<>();
  private final List<RenderBucketRenderNode> renderNodesLastFrame = new ArrayList<>();
  private final BatchManager localBatchManager = new BatchManager();

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
      final NiftyConfiguration config,
      final Statistics stats) {
    this.rect = rect;
    this.context = createContext(rect, renderDevice, stats);
    clearContext(rect, renderDevice);
    this.bucketTransform = Mat4.createTranslate(rect.getOrigin().getX(), rect.getOrigin().getY(), 0.f);
    this.bucketTransformInverse = Mat4.invert(bucketTransform, null);
    this.config = config;
    this.used = false;
  }

  public void begin() {
    renderNodes.clear();
  }

  public void update(final RenderBucketRenderNode renderNode) {
    if (renderNode.getScreenSpaceAABB().isOverlapping(rect)) {
      renderNodes.add(renderNode);
    }
  }

  public void render(final BatchManager batchManager, final NiftyRenderDevice renderDevice) {
    if (changed()) {
      if (!renderNodes.isEmpty()) {
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
                config.getShowRenderNodesOverlayColor(),
                bucketTransformInverse);
          }
        }

        if (config.isShowRenderBuckets()) {
          context.setFillColor(debugOverlayColor);
          context.filledRect(0., 0., rect.getSize().getWidth(), rect.getSize().getHeight());
        }

        context.flush();
        used = true;
      } else if (used) {
        clearContext(rect, renderDevice);
        used = false;
      }
    }

    batchManager.addTextureQuad(context.getNiftyTexture(), bucketTransform, NiftyColor.white());

    renderNodesLastFrame.clear();
    renderNodesLastFrame.addAll(renderNodes);
  }

  private boolean changed() {
    if (renderNodes.size() != renderNodesLastFrame.size()) {
      return true;
    }
    for (int i=0; i<renderNodes.size(); i++) {
      RenderBucketRenderNode renderNode = renderNodes.get(i);
      if (renderNode.isChanged()) {
        return true;
      }
    }
    return false;
  }

  private Context createContext(final NiftyRect rect, final NiftyRenderDevice renderDevice, final Statistics stats) {
    return new Context(
          renderDevice.createTexture(
              Math.round(rect.getSize().getWidth()),
              Math.round(rect.getSize().getHeight()),
              NiftyRenderDevice.FilterMode.Linear),
          renderDevice.createTexture(
              Math.round(rect.getSize().getWidth()),
              Math.round(rect.getSize().getHeight()),
              NiftyRenderDevice.FilterMode.Linear),
          stats);
  }

  private void clearContext(final NiftyRect rect, final NiftyRenderDevice renderDevice) {
    context.bind(renderDevice, localBatchManager);
    context.prepare();
    context.setFillColor(NiftyColor.transparent());
    context.filledRect(0., 0., rect.getSize().getWidth(), rect.getSize().getHeight());
    context.flush();
  }

}
