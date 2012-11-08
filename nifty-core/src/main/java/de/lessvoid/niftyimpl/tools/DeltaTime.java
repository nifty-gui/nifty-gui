package de.lessvoid.niftyimpl.tools;

import de.lessvoid.nifty.spi.TimeProvider;

public class DeltaTime {
  private final TimeProvider time;
  private long last;

  public DeltaTime(final TimeProvider time) {
    this.time = time;
    this.last = time.getMsTime();
  }

  public long getDelta() {
    long now = time.getMsTime();
    long delta = now - last;
    last = now;
    return delta;
  }

  public long getTime() {
    return time.getMsTime();
  }
}
