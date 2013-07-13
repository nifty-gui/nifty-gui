package de.lessvoid.nifty.api;

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

    public FrameInfo(final long frame, final long renderTime, final long updateTime, final long syncTime) {
      this.frame = frame;
      this.renderTime = renderTime;
      this.updateTime = updateTime;
      this.syncTime = syncTime;
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
  }

  private final Statistics statistics;

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
}
