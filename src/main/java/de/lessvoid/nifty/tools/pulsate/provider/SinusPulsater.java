package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;

import de.lessvoid.nifty.tools.pulsate.PulsateProvider;

/**
 * SinusPulsater.
 * @author void
 */
public class SinusPulsater implements PulsateProvider {

  /**
   * constant to half things.
   */
  private static final float HALF = 0.5f;

  /**
   * period for one full cycle in ms.
   */
  private float period;

  /**
   * start time.
   */
  private float startTime;

  /**
   * cycle (true) or one shot (false).
   */
  private boolean cycle = true;

  /**
   * Initialize the Pulsater.
   * @param parameter the parameters
   */
  public void initialize(final Properties parameter) {
    this.period = Float.parseFloat(parameter.getProperty("period", "1000"));
    this.cycle = "true".equals(parameter.getProperty("cycle", "true").toLowerCase());
  }

  /**
   * Get current value for the given time.
   * @param msTime current time
   * @return the pulsate value in [0,1] interval
   */
  public float getValue(final long msTime) {
    float t = msTime - startTime;
    if (cycle) {
      return getSinusValue(t);
    } else {
      if (t > period * HALF) {
        return 1.0f;
      } else {
        return getSinusValue(t);
      }
    }
  }

  /**
   * get scaled sinus.
   * @param x input
   * @return sinus
   */
  private float getSinusValue(final float x) {
    return (float) Math.abs(Math.sin((Math.PI * x / period)));
  }

  /**
   * Reset.
   * @param msTime current time
   */
  public void reset(final long msTime) {
    this.startTime = msTime;
  }
}
