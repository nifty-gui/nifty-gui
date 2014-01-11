package de.lessvoid.nifty.api;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import de.lessvoid.nifty.internal.common.Statistics;

/**
 * Several different statistical informations about Niftys internal processing.
 * @author void
 */
public class NiftyStatistics implements NiftyStatisticsMXBean {
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

    public FrameInfo(
        final long frame,
        final long renderTime,
        final long updateTime,
        final long syncTime,
        final long renderBatchCount) {
      this.frame = frame;
      this.renderTime = renderTime;
      this.updateTime = updateTime;
      this.syncTime = syncTime;
      this.renderBatchCount = renderBatchCount;
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

    try {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
      ObjectName name = new ObjectName("de.lessvoid.nifty:type=NiftyStatistics"); 
      mbs.registerMBean(this, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  Statistics getImpl() {
    return statistics;
  }

  @Override
  public List<String> getStatistics() {
    List<String> stuff = new ArrayList<String>();
    FrameInfo[] frameInfos = getAllSamples();
    stuff.add("     frame    update     render      synch   batchc.\n");
    StringBuilder line = new StringBuilder();
    for (FrameInfo frameInfo : frameInfos) {
      line.setLength(0);
      line.append(String.format("%10s", frameInfo.getFrame()));
      line.append(formatValue(frameInfo.getUpdateTime()));
      line.append(formatValue(frameInfo.getRenderTime()));
      line.append(formatValue(frameInfo.getSyncTime()));
      line.append(formatDirect(frameInfo.getRenderBatchCount()));
      line.append("\n");
      System.out.println(line);
      stuff.add(line.toString());
    }
    return stuff;
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
