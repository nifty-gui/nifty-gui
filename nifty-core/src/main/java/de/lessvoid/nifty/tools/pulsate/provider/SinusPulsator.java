package de.lessvoid.nifty.tools.pulsate.provider;

import de.lessvoid.nifty.tools.pulsate.PulsatorProvider;

import javax.annotation.Nonnull;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SinusPulsator.
 *
 * @author void
 */
public class SinusPulsator implements PulsatorProvider {
  /**
   * the logger.
   */
  @Nonnull
  private static final Logger log = Logger.getLogger(SinusPulsator.class.getName());

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
  private long startTime;

  /**
   * cycle (true) or one shot (false).
   */
  private boolean cycle = true;

  /**
   * Initialize the Pulsator.
   *
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
    this.cycle = Boolean.parseBoolean(parameter.getProperty("cycle", Boolean.toString(true)));
  }

  /**
   * Get current value for the given time.
   *
   * @param msTime current time
   * @return the pulsate value in [0,1] interval
   */
  @Override
  public float getValue(final long msTime) {
    long t = msTime - startTime;
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
   *
   * @param x input
   * @return sinus
   */
  private float getSinusValue(final long x) {
    return (float) Math.abs(Math.sin((Math.PI * x / period)));
  }

  /**
   * Reset.
   *
   * @param msTime current time
   */
  @Override
  public void reset(final long msTime) {
    this.startTime = msTime;
  }
}
