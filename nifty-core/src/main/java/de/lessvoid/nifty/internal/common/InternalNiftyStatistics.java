package de.lessvoid.nifty.internal.common;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.lessvoid.nifty.api.NiftyStatistics.FrameInfo;

public class InternalNiftyStatistics {
  private static final int TIME_HISTORY = 10;

  private int frameCounter = 0;
  private final Queue<FrameInfo> frameHistory = new LinkedBlockingQueue<FrameInfo>(TIME_HISTORY);
  private final long[] times = new long[Type.values().length];

  public enum Type {
    Synchronize,
    Update,
    Render
  }

  /**
   * Should be called when processing for a frame ends. This will put all the data we've recorded into a new
   * FrameInfo instance and will store it for later retrieval.
   */
  public void endFrame() {
    addSample(new FrameInfo(
        frameCounter++,
        times[Type.Render.ordinal()],
        times[Type.Update.ordinal()],
        times[Type.Synchronize.ordinal()]));
    for (Type type : Type.values()) {
      times[type.ordinal()] = -1;
    }
  }

  /**
   * Start a sample time.
   * @param type the Type that is being recorded
   */
  public void start(final Type type) {
    times[type.ordinal()] = System.nanoTime();
  }

  /**
   * Stop a sample time.
   * @param type the Type to stop
   */
  public void stop(final Type type) {
    times[type.ordinal()] = System.nanoTime() - times[type.ordinal()];
  }

  /**
   * Get all samples collected so far.
   * @return FrameInfo array
   */
  public FrameInfo[] getFrameInfos() {
    return frameHistory.toArray(new FrameInfo[0]);
  }

  private void addSample(final FrameInfo frameInfo) {
    if (frameHistory.offer(frameInfo)) {
      return;
    }
    frameHistory.poll();
    addSample(frameInfo);
  }
}
