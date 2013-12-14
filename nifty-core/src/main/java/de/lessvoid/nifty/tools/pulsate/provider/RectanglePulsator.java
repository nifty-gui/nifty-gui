package de.lessvoid.nifty.tools.pulsate.provider;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.tools.pulsate.PulsatorProvider;

import javax.annotation.Nonnull;

/**
 * SinusPulsator.
 * @author void
 */
public class RectanglePulsator implements PulsatorProvider {
  /**
   * the logger.
   */
  @Nonnull
  private static final Logger log = Logger.getLogger(RectanglePulsator.class.getName());

  /**
   * period  for the sinus.
   */
  private float period;

  /**
   * start time.
   */
  private float startTime;

  /**
   * Initialize the Pulsator.
   * @param parameter the parameters
   */
  @Override
  public void initialize(@Nonnull final Properties parameter) {
    try {
      period = Float.parseFloat(parameter.getProperty("period", "1000"));
    } catch (NumberFormatException e) {
      log.log(Level.SEVERE, "Failed to parse \"period\" value.", e);
      period = 1000.f;
    }
  }

  /**
   * Get current value for the given time.
   * @param msTime current time
   * @return the pulsate value in [0,1] interval
   */
  @Override
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
  @Override
  public void reset(final long msTime) {
    this.startTime = msTime;
  }
}
