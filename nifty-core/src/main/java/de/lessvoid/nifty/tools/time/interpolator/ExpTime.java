package de.lessvoid.nifty.tools.time.interpolator;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * Interpolates a value exponential from 0.0 to 1.0 with the given time parameters.
 * @author void
 */
public class ExpTime implements Interpolator {

  /**
   * factor.
   */
  private float factorParam = 0;

  @Override
  public final void initialize(@Nonnull final Properties parameter) {
    this.factorParam = Float.parseFloat(parameter.getProperty("factor", "1"));
  }

  @Override
  public void start() {
  }

  @Override
  public final float getValue(final long lengthParameter, final long timePassed) {
    return (float) Math.pow(1.0f - ((lengthParameter - timePassed) / (float) lengthParameter), factorParam);
  }
}
