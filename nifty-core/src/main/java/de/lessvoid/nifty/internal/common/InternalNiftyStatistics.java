package de.lessvoid.nifty.internal.common;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InternalNiftyStatistics {
  private static final int TIME_HISTORY = 10;
  private final AtomicInteger renderTreeSynchronisations = new AtomicInteger();
  private final Queue<Integer> frameTimeHistory = new LinkedBlockingQueue<Integer>(TIME_HISTORY);
  private final Queue<Integer> updateTimeHistory = new LinkedBlockingQueue<Integer>(TIME_HISTORY);

  public void renderTreeSynchronisation() {
    renderTreeSynchronisations.addAndGet(1);
  }

  public void addRenderTime(final int time) {
    addTime(frameTimeHistory, time);
  }

  public void addUpdateTime(final int time) {
    addTime(updateTimeHistory, time);
  }

  public int getRenderTreeSynchronisations() {
    return renderTreeSynchronisations.get();
  }

  public void getFrameTime(final List<Integer> target) {
    getTime(frameTimeHistory, target);
  }

  public void getUpdateTime(final List<Integer> target) {
    getTime(updateTimeHistory, target);
  }

  private void addTime(final Queue<Integer> queue, final int time) {
    if (queue.offer(time)) {
      return;
    }
    queue.poll();
    addTime(queue, time);
  }

  private void getTime(final Queue<Integer> queue, final List<Integer> target) {
    target.clear();

    Iterator<Integer> iter = queue.iterator();
    while (iter.hasNext()) {
      target.add(iter.next());
    }
  }
}
