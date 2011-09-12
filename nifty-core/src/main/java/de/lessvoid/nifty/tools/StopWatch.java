package de.lessvoid.nifty.tools;

public class StopWatch {
  private TimeProvider timeProvider;
  private long startTime;

  public StopWatch(final TimeProvider timeProviderParam) {
    timeProvider = timeProviderParam;
  }

  public long stop() {
    return timeProvider.getMsTime() - startTime;
  }

  public void start() {
    startTime = timeProvider.getMsTime();
  }
}
