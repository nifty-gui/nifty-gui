package de.lessvoid.nifty.tools;

public class StopWatch {
  private de.lessvoid.nifty.spi.time.TimeProvider timeProvider;
  private long startTime;

  public StopWatch(final de.lessvoid.nifty.spi.time.TimeProvider timeProviderParam) {
    timeProvider = timeProviderParam;
  }

  public long stop() {
    return timeProvider.getMsTime() - startTime;
  }

  public void start() {
    startTime = timeProvider.getMsTime();
  }
}
