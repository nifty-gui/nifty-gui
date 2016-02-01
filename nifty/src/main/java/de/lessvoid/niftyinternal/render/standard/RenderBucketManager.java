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

import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;
import de.lessvoid.nifty.NiftyConfiguration;
import de.lessvoid.niftyinternal.common.Statistics;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by void on 17.01.16.
 */
class RenderBucketManager {
  // We keep all RenderBucketRenderNode instances in this list of RenderBucket. A RenderBucket is a part of the screen
  // that is cached in a texture. When rendering the screen we only re-render RenderBuckets that contain changed
  // RenderBucketRenderNode instances. A RenderBucketRenderNode can be a part of multiple RenderBuckets. When a
  // RenderBucketRenderNode overlaps multiple buckets it is stored in all of the ones that overlaps.
  private final List<RenderBucket> renderBuckets;
  private final Statistics stats;

  public RenderBucketManager(
      final NiftyRenderDevice renderDevice,
      final NiftyConfiguration niftyConfiguration,
      final Statistics stats) {
    this.stats = stats;
    this.renderBuckets = createBuckets(renderDevice, niftyConfiguration);
  }

  public void begin() {
    for (int i=0; i<renderBuckets.size(); i++) {
      renderBuckets.get(i).begin();
    }
  }

  public void update(final RenderBucketRenderNode renderNode) {
    for (int i=0; i<renderBuckets.size(); i++) {
      renderBuckets.get(i).update(renderNode);
    }
  }

  public void render(final BatchManager batchManager, final NiftyRenderDevice renderDevice) {
    batchManager.addChangeCompositeOperation(NiftyCompositeOperation.SourceOver);
    for (int i=0; i<renderBuckets.size(); i++) {
      renderBuckets.get(i).render(batchManager, renderDevice);
    }
  }

  private List<RenderBucket> createBuckets(final NiftyRenderDevice renderDevice, NiftyConfiguration niftyConfiguration) {
    int bucketHeight = niftyConfiguration.getRenderBucketHeight();
    int bucketWidth = niftyConfiguration.getRenderBucketWidth();
    int numBucketsY = renderDevice.getDisplayHeight() / bucketHeight;
    int numBucketsX = renderDevice.getDisplayWidth() / bucketWidth;

    List<RenderBucket> result = new ArrayList<>();
    for (int y=0; y<numBucketsY; y++) {
      for (int x=0; x<numBucketsX; x++) {
        result.add(
            new RenderBucket(
                NiftyRect.newNiftyRect(
                    NiftyPoint.newNiftyPoint(x * bucketWidth, y * bucketHeight),
                    NiftySize.newNiftySize(bucketWidth, bucketHeight)
                ),
                renderDevice,
                niftyConfiguration,
                stats));
      }
    }
    return result;
  }
}
