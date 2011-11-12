package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

/**
 * Interpolates a value linear from 0.0 to 1.0 with the given time parameters.
 * @author void
 */
public class LinearTime implements Interpolator {

  /**
   * initialize.
   * @param parameter Properties
   */
  public void initialize(final Properties parameter) {
  }

  public void start() {
  }

  /**
   * get value.
   * @param lengthParam max length in ms
   * @param timePassed time already passed in ms
   * @return calculated value
   */
  public final float getValue(final long lengthParam, final long timePassed) {
    return 1.0f - ((lengthParam - timePassed) / (float) lengthParam);
  }
}
