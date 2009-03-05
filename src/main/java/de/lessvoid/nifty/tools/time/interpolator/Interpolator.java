package de.lessvoid.nifty.tools.time.interpolator;

import java.util.Properties;

/**
 * Interface to connect different interpolation algorithm to the TimeInterpolator class.
 * @author void
 */
public interface Interpolator {

  /**
   * initialize.
   * @param parameter Properties used to parameterize this class
   */
  void initialize(Properties parameter);

  /**
   * In case the Interpolator wants to set some value when started.
   */
  void start();

  /**
   * get a new value.
   * @param lengthParameter max length of effect in ms
   * @param timePassed time already passed in ms
   * @return the value
   */
  float getValue(long lengthParameter, long timePassed);
}
