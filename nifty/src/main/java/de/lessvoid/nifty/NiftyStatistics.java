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
package de.lessvoid.nifty;

import de.lessvoid.niftyinternal.common.Statistics;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private final long renderStatePassTime;
    private final long renderContentPassTime;
    private final long renderPassTime;
    private final long updateTime;
    private final long renderBatchCount;
    private final long inputProcessingUpdateTime;
    private final long totalFrameTime; // for now this requires nifty.update() immediately followed by nifty.render() to be correct

    public FrameInfo(
        final long frame,
        final long renderTime,
        final long updateTime,
        final long renderStatePassTime,
        final long renderContentPassTime,
        final long renderPassTime,
        final long renderBatchCount,
        final long inputProcessingUpdateTime,
        final long totalFrameTime) {
      this.frame = frame;
      this.renderTime = renderTime;
      this.renderStatePassTime = renderStatePassTime;
      this.renderContentPassTime = renderContentPassTime;
      this.renderPassTime = renderPassTime;
      this.updateTime = updateTime;
      this.renderBatchCount = renderBatchCount;
      this.inputProcessingUpdateTime = inputProcessingUpdateTime;
      this.totalFrameTime = totalFrameTime;
    }

    public long getFrame() {
      return frame;
    }

    public long getRenderTime() {
      return renderTime;
    }

    public long getRenderStatePassTime() {
      return renderStatePassTime;
    }

    public long getRenderContentPassTime() {
      return renderContentPassTime;
    }

    public long getRenderPassTime() {
      return renderPassTime;
    }

    public long getUpdateTime() {
      return updateTime;
    }

    public long getRenderBatchCount() {
      return renderBatchCount;
    }

    public long getInputProcessingUpdateTime() {
      return inputProcessingUpdateTime;
    }

    public long getTotalFrameTime() {
      return totalFrameTime;
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
    List<String> stuff = new ArrayList<>();
    FrameInfo[] frameInfos = getAllSamples();
    stuff.add("      frame     update     render    r.state  r.content     r.pass    # batch      input      total        fps\n");
    StringBuilder line = new StringBuilder();
    for (FrameInfo frameInfo : frameInfos) {
      line.setLength(0);
      line.append(String.format("%11s", frameInfo.getFrame()));
      line.append(formatValue(toMS(frameInfo.getUpdateTime())));
      line.append(formatValue(toMS(frameInfo.getRenderTime())));
      line.append(formatValue(toMS(frameInfo.getRenderStatePassTime())));
      line.append(formatValue(toMS(frameInfo.getRenderContentPassTime())));
      line.append(formatValue(toMS(frameInfo.getRenderPassTime())));
      line.append(formatDirect(frameInfo.getRenderBatchCount()));
      line.append(formatValue(toMS(frameInfo.getInputProcessingUpdateTime())));
      line.append(formatValue(toMS(frameInfo.getTotalFrameTime())));
      line.append(formatValue(1000./toMS(frameInfo.getTotalFrameTime())));
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

  private String formatValue(final double value) {
    if (value == -1) {
      return String.format("%11s", "N/A");
    }
    NumberFormat format = NumberFormat.getInstance(Locale.US);
    format.setGroupingUsed(false);
    format.setMinimumFractionDigits(4);
    format.setMaximumFractionDigits(4);
    return String.format("%11s", format.format(value));
  }

  private String formatDirect(final long value) {
    if (value == -1) {
      return String.format("%11s", "N/A");
    }
    return String.format("%11s", value);
  }

  private double toMS(final double value) {
    return value / 100000.;
  }
}
