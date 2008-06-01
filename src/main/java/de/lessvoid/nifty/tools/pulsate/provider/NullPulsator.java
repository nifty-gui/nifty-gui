package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;

import de.lessvoid.nifty.tools.pulsate.PulsatorProvider;

/**
 * The NullPulsator does not really pulsate =).
 * @author void
 */
public class NullPulsator implements PulsatorProvider {

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
