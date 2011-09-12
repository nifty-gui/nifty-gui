package de.lessvoid.nifty.tools.pulsate;

import java.util.Properties;

/**
 * The actual Pulsator.
 * @author void
 *
 */
public interface PulsatorProvider {

  /**
   * Initialize the parameter.
   * @param parameter properties
   */
  void initialize(Properties parameter);

  /**
   * Get the value.
   * @param msTime current time
   * @return the value in [0,1] range
   */
  float getValue(long msTime);

  /**
   * Reset.
   * @param msTime current time
   */
  void reset(long msTime);

}
