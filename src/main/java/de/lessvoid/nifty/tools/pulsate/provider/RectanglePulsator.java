package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;

import de.lessvoid.nifty.tools.pulsate.PulsatorProvider;

/**
 * SinusPulsater.
 * @author void
 */
public class RectanglePulsator implements PulsatorProvider {

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
    this.scaleFactor = Float.parseFloat(parameter.getProperty("scaleFactor", "200"));
  }

  /**
   * Get current value for the given time.
   * @param msTime current time
   * @return the pulsate value in [0,1] intervall
   */
  public float getValue(final long msTime) {
    float value = (float) (Math.signum(Math.sin((msTime - startTime) * scaleFactor)) + 1.0f) / 2.0f;
    return value;
  }

  /**
   * Reset.
   * @param msTime current time in ms
   */
  public void reset(final long msTime) {
    this.startTime = msTime;
  }
}
