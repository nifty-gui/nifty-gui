package de.lessvoid.nifty.tools.time;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.time.interpolator.ExpTime;
import de.lessvoid.nifty.tools.time.interpolator.Interpolator;
import de.lessvoid.nifty.tools.time.interpolator.LinearTime;
import de.lessvoid.nifty.tools.time.interpolator.NullTime;
import de.lessvoid.nifty.tools.time.interpolator.OneTime;

/**
 * TimeProvider class.
 * @author void
 */
public class TimeInterpolator {

  /**
   * the logger.
   */
  private static Logger log = Logger.getLogger(TimeInterpolator.class.getName());

  /**
   * the time provider.
   */
  private final TimeProvider timeProvider;

  /**
   * the InterpolatorProvider we use.
   */
  private Interpolator interpolatorProvider;

  /**
   * the current value [0,1].
   */
  private float value = 0.0f;

  /**
   * the start time in ms the effect starts.
   */
  private long startTime;

  /**
   * the maximum time in ms.
   */
  private long lengthParam;

  /**
   * start delay time in ms.
   */
  private long startDelayParam = 0;

  /**
   * Initialize with the given parameters.
   * <p />
   * This function automatically calls {@link #initialize(java.util.Properties, boolean)}  so calling this one again
   * is <b>not</b> needed.
   *
   * @param parameter parameter props
   * @param newTimeProvider TimeProvider to use
   * @param infinite infinite effects never end
   */
  public TimeInterpolator(final Properties parameter, final TimeProvider newTimeProvider, final boolean infinite) {
    timeProvider = newTimeProvider;
    initialize(parameter, infinite);
  }

  /**
   * Initialize the time interpolator with the given values.
   * <p />
   * This possible to call this method more then once in case the parameters got changed.
   *
   * @param parameter the parameters used for the initialization
   * @param infinite the infinite flag, in case its {@code true} this effect does never end
   */
  public void initialize(final Properties parameter, final boolean infinite) {
    startDelayParam = Long.parseLong(parameter.getProperty("startDelay", "0"));

    String lengthDefault = null;
    if (infinite) {
      lengthDefault = "infinite";
    }

    interpolatorProvider = null;
    if ("infinite".equals(parameter.getProperty("length", lengthDefault))) {
      interpolatorProvider = new NullTime();
    } else {
      lengthParam = Long.parseLong(parameter.getProperty("length", "1000"));
      if (Boolean.parseBoolean(parameter.getProperty("oneShot"))) {
        interpolatorProvider = new OneTime();
      }
    }

    // check for the given timeType to create the appropriate interpolator
    if (interpolatorProvider == null) {
      String timeType = parameter.getProperty("timeType", "linear");
      if (timeType.equals("infinite")) {
        interpolatorProvider = new NullTime();
      } else if (timeType.equals("linear")) {
        interpolatorProvider = new LinearTime();
      } else if (timeType.equals("exp")) {
        interpolatorProvider = new ExpTime();
      } else {
        log.warning(timeType + " is not supported, using NullTime for fallback. probably not what you want...");
        interpolatorProvider = new NullTime();
      }
    }

    // initialize the provider
    interpolatorProvider.initialize(parameter);
  }

  /**
   * start the interpolation.
   */
  public void start() {
    interpolatorProvider.start();
    value = 0.0f;
    startTime = timeProvider.getMsTime() + startDelayParam;
  }

  /**
   * update the value.
   * @return true when still active and false when done
   */
  public final boolean update() {
    long now = timeProvider.getMsTime();
    long timePassed = now - startTime;

    if (timePassed < 0) {
      return true;
    }

    this.value = interpolatorProvider.getValue(lengthParam, timePassed);

    if (this.value > 1.0f) {
      this.value = 1.0f;
      return false;
    } else {
      return true;
    }
  }

  /**
   * get the current value [0.0, 1.0].
   * @return the current value
   */
  public final float getValue() {
    return value;
  }
}
