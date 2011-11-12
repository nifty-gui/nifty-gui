package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

public class OneTime implements Interpolator {
  private boolean hasRun = false;

  public final void initialize(final Properties parameter) {
  }

  public void start() {
    hasRun = false;
  }

  public final float getValue(final long lengthParam, final long timePassed) {
    if (!hasRun) {
      hasRun = true;
      return 0.0f;
    } else {
      return 2.0f;
    }
  }
}
