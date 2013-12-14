package de.lessvoid.nifty.tools.time.interpolator;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * This interpolator returns {@code 0.f} at its first run and {@code 2.f} during all following runs.
 */
public class OneTime implements Interpolator {
  /**
   * Storage if the interpolator already run once.
   */
  private boolean hasRun;

  @Override
  public final void initialize(@Nonnull final Properties parameter) {
    hasRun = false;
  }

  @Override
  public void start() {
    hasRun = false;
  }

  @Override
  public final float getValue(final long lengthParam, final long timePassed) {
    if (!hasRun) {
      hasRun = true;
      return 0.0f;
    } else {
      return 2.0f;
    }
  }
}
