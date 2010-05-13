package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;

import de.lessvoid.nifty.tools.pulsate.PulsatorProvider;

/**
 * SinusPulsater.
 * @author void
 */
public class RectanglePulsator implements PulsatorProvider {

  /**
   * period  for the sinus.
   */
  private float period;

  /**
   * start time.
   */
  private float startTime;

  /**
   * Initialize the Pulsater.
   * @param parameter the parameters
   */
  public void initialize(final Properties parameter) {
    this.period = Float.parseFloat(parameter.getProperty("period", "1000"));
  }

  /**
   * Get current value for the given time.
   * @param msTime current time
   * @return the pulsate value in [0,1] intervall
   */
  public float getValue(final long msTime) {
    float delta = msTime - (long)startTime;
    double t = Math.PI * delta / period;
    double sin = Math.sin(t);
    double signum = Math.signum(sin);
    double s = signum + 1.0f;
    return (float) s / 2.0f;
  }

  /**
   * Reset.
   * @param msTime current time in ms
   */
  public void reset(final long msTime) {
    this.startTime = msTime;
  }
}
