package de.lessvoid.nifty.internal.animate;

import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.spi.TimeProvider;

public class IntervalAnimator {
  // we need the time
  private final TimeProvider timeProvider;

  // the delay before the animated request should begin
  private final long delay;

  // the interval between redraws
  private final long interval;

  // the start time (when the animatedRequestRedraw has been enabled for this node)
  private final long startTime;

  // we want to only toggle requestRedraw once when we reach the next interval. To do this we remember the last
  // sign state and only set requestRedraw to true when it changes.
  private boolean lastState;

  // the action we'll call when the interval matches
  private final NiftyCallback<Float> callback;

  public IntervalAnimator(
      final TimeProvider timeProvider,
      final long delay,
      final long interval,
      final NiftyCallback<Float> callback) {
    this.timeProvider = timeProvider;
    this.delay = delay;
    this.interval = interval;
    this.startTime = timeProvider.getMsTime();
    this.lastState = false;
    this.callback = callback;
  }

  public void update() {
    long currentTime = timeProvider.getMsTime();
    long deltaTime = currentTime - startTime;
    if (deltaTime < delay) {
      return;
    }

    // correct deltaTime for the start time so that we start with 0
    boolean isActive = getValue(deltaTime - delay);
    if (isActive != lastState) {
      lastState = isActive;
      callback.execute(deltaTime / 1000.f);
    }
  }

  private boolean getValue(final long delta) {
    double t = Math.PI * delta / (double) interval;
    double sin = Math.sin(t);
    return sin > 0;
  }
}
