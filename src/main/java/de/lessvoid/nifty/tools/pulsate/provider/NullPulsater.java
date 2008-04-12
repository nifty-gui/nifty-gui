package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;

import de.lessvoid.nifty.tools.pulsate.PulsateProvider;

/**
 * The NullPulsate does not really pulsates =).
 *  * @author void
 */
public class NullPulsater implements PulsateProvider {

  /**
   * Actually does nothing.
   * @param parameter the parameters
   */
  public void initialize(final Properties parameter) {
  }

  /**
   * Always returns 0.
   * @param msTime the time
   * @return always returns 0
   */
  public float getValue(final long msTime) {
    return 0;
  }

  /**
   * Reset.
   * @param msTime the time
   */
  public void reset(final long msTime) {
  }
}
