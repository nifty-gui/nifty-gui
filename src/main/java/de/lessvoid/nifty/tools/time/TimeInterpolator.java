package de.lessvoid.nifty.tools.time;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.time.interpolator.ExpTime;
import de.lessvoid.nifty.tools.time.interpolator.Interpolator;
import de.lessvoid.nifty.tools.time.interpolator.LinearTime;
import de.lessvoid.nifty.tools.time.interpolator.NullTime;

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
  private TimeProvider timeProvider;

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
   * initialize with the given parameters.
   *
   * @param parameter parameter props
   * @param newTimeProvider TimeProvider to use
   */
  public TimeInterpolator(final Properties parameter, final TimeProvider newTimeProvider) {
    this.timeProvider = newTimeProvider;

    this.lengthParam = Long.parseLong(parameter.getProperty("length", "1000"));
    this.startDelayParam = Long.parseLong(parameter.getProperty("startDelay", "0"));

    // check for the given timeType to create the appropriate interpolator
    String timeType = parameter.getProperty("timeType", "linear");
    if (timeType == "infinite") {
      interpolatorProvider = new NullTime();
    } else if (timeType.equals("linear")) {
      interpolatorProvider = new LinearTime();
    } else if (timeType.equals("exp")) {
      interpolatorProvider = new ExpTime();
    } else {
      log.warning(timeType + " is not supported, using NullTime for fallback. probably not what you want...");
      interpolatorProvider = new NullTime();
    }

    // initialize the provider
    interpolatorProvider.initialize(parameter);
  }

  /**
   * start the interpolation.
   */
  public final void start() {
    this.value = 0.0f;
    this.startTime = timeProvider.getMsTime() + startDelayParam;
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
