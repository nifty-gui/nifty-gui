package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

/**
 * Interpolator that always return 1.0f.
 * @author void
 */
public class NullTime implements Interpolator {

  /**
   * initialize.
   * @param parameter Properties used to parameterize this class
   */
  public void initialize(final Properties parameter) {
  }

  public void start() {
  }

  /**
   * get a new value.
   * @param lengthParameter max length of effect in ms
   * @param timePassed time already passed in ms
   * @return the value
   */
  public float getValue(final long lengthParameter, final long timePassed) {
    return 1.0f;
  }
}
