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
package de.lessvoid.niftyinternal.common;

import de.lessvoid.nifty.NiftyStatistics.FrameInfo;
import de.lessvoid.nifty.spi.TimeProvider;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Statistics {
  private static final int TIME_HISTORY = 10;

  private final TimeProvider timeProvider;
  private int frameCounter = 0;
  private final Queue<FrameInfo> frameHistory = new LinkedBlockingQueue<>(TIME_HISTORY);
  private final long[] times = new long[Type.values().length];

  private int fpsFrameCounter = 0;
  private long now = 0;
  private int fpsFrames = -1;

  private enum Type {
    Update,
    Render,
    RenderStatePass,
    RenderContentPass,
    RenderPass,
    RenderBatchCount,
    InputProcessing,
    TotalFrameTime
  }

  public Statistics(final TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
    this.now = timeProvider.getMsTime();
  }

  public void startFrame() {
    for (Type type : Type.values()) {
      times[type.ordinal()] = -1;
    }
    times[Type.RenderBatchCount.ordinal()] = 0;
    start(Type.TotalFrameTime);
  }

  /**
   * Should be called when processing for a frame ends. This will put all the data we've recorded into a new
   * FrameInfo instance and will store it for later retrieval.
   */
  public void endFrame() {
    stop(Type.TotalFrameTime);
    addSample(new FrameInfo(
        frameCounter++,
        times[Type.Render.ordinal()],
        times[Type.Update.ordinal()],
        times[Type.RenderStatePass.ordinal()],
        times[Type.RenderContentPass.ordinal()],
        times[Type.RenderPass.ordinal()],
        times[Type.RenderBatchCount.ordinal()],
        times[Type.InputProcessing.ordinal()],
        times[Type.TotalFrameTime.ordinal()]));
  }

  /**
   * Get all samples collected so far.
   * @return FrameInfo array
   */
  public FrameInfo[] getFrameInfos() {
    return frameHistory.toArray(new FrameInfo[0]);
  }

  public int getFrames() {
    return fpsFrames;
  }

  public void startUpdate() {
    start(Type.Update);
  }

  public void stopUpdate() {
    stop(Type.Update);
  }

  public void startInputProcessing() {
    start(Type.InputProcessing);
  }

  public void stopInputProcessing() {
    stop(Type.InputProcessing);
  }

  public void startRender() {
    start(Type.Render);
  }

  public void stopRender() {
    stop(Type.Render);

    fpsFrameCounter++;
    long diff = timeProvider.getMsTime() - now;
    if (diff >= 1000) {
      now += diff;
      fpsFrames = fpsFrameCounter;
      fpsFrameCounter = 0;
    }
  }

  public void startRenderStatePass() {
    start(Type.RenderStatePass);
  }

  public void stopRenderStatePass() {
    stop(Type.RenderStatePass);
  }

  public void startRenderContentPass() {
    start(Type.RenderContentPass);
  }

  public void stopRenderContentPass() {
    stop(Type.RenderContentPass);
  }

  public void startRenderPass() {
    start(Type.RenderPass);
  }

  public void stopRenderPass() {
    stop(Type.RenderPass);
  }

  public void incBatchCount(final int batchCount) {
    times[Type.RenderBatchCount.ordinal()] += batchCount;
  }

  private void addSample(final FrameInfo frameInfo) {
    if (frameHistory.offer(frameInfo)) {
      return;
    }
    frameHistory.poll();
    addSample(frameInfo);
  }

  private void start(final Type type) {
    times[type.ordinal()] = System.nanoTime();
  }

  private void stop(final Type type) {
    times[type.ordinal()] = System.nanoTime() - times[type.ordinal()];
  }
}
