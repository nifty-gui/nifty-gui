package de.lessvoid.nifty.tools.pulsate;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.provider.NullPulsater;
import de.lessvoid.nifty.tools.pulsate.provider.RectanglePulsater;
import de.lessvoid.nifty.tools.pulsate.provider.SinusPulsater;

/**
 * Pulsater class.
 * @author void
 */
public class Pulsater {

  /**
   * the logger.
   */
  private static Log log = LogFactory.getLog(Pulsater.class);

  /**
   * the time provider.
   */
  private TimeProvider timeProvider;

  /**
   * the PulsateProvider we use.
   */
  private PulsateProvider pulsateProvider;

  /**
   * initialize with the given parameters.
   *
   * @param parameter parameter props
   * @param newTimeProvider TimeProvider to use
   */
  public Pulsater(final Properties parameter, final TimeProvider newTimeProvider) {
    this.timeProvider = newTimeProvider;

    // check for the given pulsateType to create the appropriate PulsateProvider
    String pulsateType = parameter.getProperty("pulsateType", "sin");
    if (pulsateType.equals("sin")) {
      pulsateProvider = new SinusPulsater();
    } else if (pulsateType.equals("rectangle")) {
      pulsateProvider = new RectanglePulsater();
    } else {
      log.error(pulsateType + " is not supported, using NullPulsater for fallback. probably not what you want...");
      pulsateProvider = new NullPulsater();
    }

    // initialize the provider
    pulsateProvider.initialize(parameter);
    reset();
  }

  /**
   * Reset the pulsater.
   */
  public void reset() {
    pulsateProvider.reset(timeProvider.getMsTime());
  }

  /**
   * update the value.
   * @return true when still active and false when done
   */
  public final float update() {
    return pulsateProvider.getValue(timeProvider.getMsTime());
  }
}
