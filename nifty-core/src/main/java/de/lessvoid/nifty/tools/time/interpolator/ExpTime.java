package de.lessvoid.nifty.tools.time.interpolator;

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

  /**
   * @param parameter parameter
   */
  public final void initialize(final Properties parameter) {
    this.factorParam = Float.parseFloat(parameter.getProperty("factor", "1"));
  }

  public void start() {
  }

  /**
   * @param lengthParameter effect length in ms
   * @param timePassed time already passed in ms
   * @return calculated value
   */
  public final float getValue(final long lengthParameter, final long timePassed) {
    return (float) Math.pow(1.0f - ((lengthParameter - timePassed) / (float) lengthParameter), factorParam);
  }
}
