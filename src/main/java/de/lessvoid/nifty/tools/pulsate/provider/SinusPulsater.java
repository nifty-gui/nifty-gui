package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;

import de.lessvoid.nifty.tools.pulsate.PulsateProvider;

/**
 * SinusPulsater.
 * @author void
 */
public class SinusPulsater implements PulsateProvider {

  /**
   * scale factor for the sinus.
   */
  private float scaleFactor;

  /**
   * start time.
   */
  private float startTime;

  /**
   * Initialize the Pulsater.
   * @param parameter the parameters
   */
  public void initialize(final Properties parameter) {
    this.scaleFactor = Float.parseFloat(parameter.getProperty("scaleFactor", "0.005"));
  }

  /**
   * Get current value for the given time.
   * @param msTime current time
   * @return the pulsate value in [0,1] intervall
   */
  public float getValue(final long msTime) {
    return (float) Math.sin((startTime - msTime) * scaleFactor);
  }

  /**
   * Reset.
   * @param msTime current time
   */
  public void reset(final long msTime) {
    this.startTime = msTime;
  }
}
