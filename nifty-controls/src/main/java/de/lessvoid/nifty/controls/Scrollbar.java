package de.lessvoid.nifty.controls;

/**
 * Scrollbar.
 *
 * @author void
 */
public interface Scrollbar extends NiftyControl {
  /**
   * Setup all parameters of this Scrollbar.
   *
   * @param value          the initial value
   * @param worldMax       the maximum value
   * @param worldPageSize  the page size
   * @param buttonStepSize the step size for button clicks
   * @param pageStepSize   the step size for page up/down or clicks on the scrollbar background
   */
  void setup(float value, float worldMax, float worldPageSize, float buttonStepSize, float pageStepSize);

  /**
   * Change the value of the scrollbar.
   *
   * @param value the new value
   */
  void setValue(float value);

  /**
   * Get the current value of the scrollbar.
   */
  float getValue();

  /**
   * Set the new maximum of the scrollbar.
   *
   * @param worldMax new maximum
   */
  void setWorldMax(float worldMax);

  /**
   * Get the current maximum of the scrollbar.
   *
   * @return current world max
   */
  float getWorldMax();

  /**
   * Set the new page size
   *
   * @param worldPageSize new page size
   */
  void setWorldPageSize(float worldPageSize);

  /**
   * Get the current page size
   *
   * @return current page size
   */
  float getWorldPageSize();

  /**
   * Set the button step size to a new value.
   *
   * @param stepSize step size
   */
  void setButtonStepSize(float stepSize);

  /**
   * Get the current button step size.
   *
   * @return step size
   */
  float getButtonStepSize();

  /**
   * Set a new page size for page up/down and clicks on the background.
   *
   * @param stepSize new step size
   */
  void setPageStepSize(float stepSize);

  /**
   * Get the current page size.
   *
   * @return page size
   */
  float getPageStepSize();
}
