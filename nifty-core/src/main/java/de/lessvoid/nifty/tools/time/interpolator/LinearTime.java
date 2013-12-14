package de.lessvoid.nifty.tools.time.interpolator;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * Interpolates a value linear from 0.0 to 1.0 with the given time parameters.
 * @author void
 */
public class LinearTime implements Interpolator {

  @Override
  public void initialize(@Nonnull final Properties parameter) {
  }

  @Override
  public void start() {
  }

  @Override
  public final float getValue(final long lengthParam, final long timePassed) {
    return 1.0f - ((lengthParam - timePassed) / (float) lengthParam);
  }
}
