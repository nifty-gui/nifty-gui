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
package de.lessvoid.nifty.api;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.lessvoid.nifty.internal.common.Statistics;

/**
 * Several different statistical informations about Niftys internal processing.
 * @author void
 */
public class NiftyStatistics {
  /**
   * A FrameInfo instance records sample times for a specific frame.
   * @author void
   */
  public static class FrameInfo {
    private final long frame;
    private final long renderTime;
    private final long updateTime;
    private final long syncTime;
    private final long renderBatchCount;
    private final long inputProcessingUpdateTime;

    public FrameInfo(
        final long frame,
        final long renderTime,
        final long updateTime,
        final long syncTime,
        final long renderBatchCount,
        final long inputProcessingUpdateTime) {
      this.frame = frame;
      this.renderTime = renderTime;
      this.updateTime = updateTime;
      this.syncTime = syncTime;
      this.renderBatchCount = renderBatchCount;
      this.inputProcessingUpdateTime = inputProcessingUpdateTime;
    }

    public long getFrame() {
      return frame;
    }

    public long getRenderTime() {
      return renderTime;
    }

    public long getUpdateTime() {
      return updateTime;
    }

    public long getSyncTime() {
      return syncTime;
    }

    public long getRenderBatchCount() {
      return renderBatchCount;
    }

    public long getInputProcessingUpdateTime() {
      return inputProcessingUpdateTime;
    }
  }

  private final Statistics statistics;
  private final StringBuilder fpsText = new StringBuilder();

  /**
   * Get all collected samples so far.
   * @return array of FrameInfo instances with statistics collected so far
   */
  public FrameInfo[] getAllSamples() {
    return statistics.getFrameInfos();
  }

  NiftyStatistics(final Statistics statistics) {
    this.statistics = statistics;
  }

  Statistics getImpl() {
    return statistics;
  }

  public List<String> getStatistics() {
    List<String> stuff = new ArrayList<String>();
    FrameInfo[] frameInfos = getAllSamples();
    stuff.add("     frame    update     render      synch   batchc.   inputUpdate\n");
    StringBuilder line = new StringBuilder();
    for (FrameInfo frameInfo : frameInfos) {
      line.setLength(0);
      line.append(String.format("%10s", frameInfo.getFrame()));
      line.append(formatValue(frameInfo.getUpdateTime()));
      line.append(formatValue(frameInfo.getRenderTime()));
      line.append(formatValue(frameInfo.getSyncTime()));
      line.append(formatDirect(frameInfo.getRenderBatchCount()));
      line.append(formatValue(frameInfo.getInputProcessingUpdateTime()));
      line.append("\n");
      stuff.add(line.toString());
    }
    return stuff;
  }

  public int getFps() {
    return statistics.getFrames();
  }

  public String getFpsText() {
    fpsText.setLength(0);
    fpsText.append("fps: ");
    fpsText.append(statistics.getFrames());
    fpsText.append(" (");
    fpsText.append(1000 / (float) statistics.getFrames());
    fpsText.append(" ms)");
    return fpsText.toString();
  }

  private String formatValue(final long value) {
    if (value == -1) {
      return "N/A";
    }
    NumberFormat format = NumberFormat.getInstance(Locale.US);
    format.setGroupingUsed(false);
    format.setMinimumFractionDigits(4);
    format.setMaximumFractionDigits(4);
    return String.format("%10s", format.format(value / 100000.f));
  }

  private String formatDirect(final long value) {
    if (value == -1) {
      return "N/A";
    }
    return String.format("%10s", value);
  }
}
