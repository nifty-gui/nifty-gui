package de.lessvoid.nifty.controls;

/**
 * The Slider interface is the Nifty control API view of a Nifty Slider.
 *
 * @author void
 */
public interface Slider extends NiftyControl {
  /**
   * Set all attributes at once.
   *
   * @param min            minimum value
   * @param max            maximum value
   * @param current        current value
   * @param stepSize       step size
   * @param buttonStepSize button step size
   */
  void setup(float min, float max, float current, float stepSize, float buttonStepSize);

  /**
   * Set the Slider value to a new value.
   *
   * @param value the new value for the slider
   */
  void setValue(float value);

  /**
   * Get the current value of the Slider.
   *
   * @return current value
   */
  float getValue();

  /**
   * Set a new minimum value for the Slider. The default value is 0.
   *
   * @param min new minimum value
   */
  void setMin(float min);

  /**
   * Get the current minimum value for the Slider.
   *
   * @return get the current minimum
   */
  float getMin();

  /**
   * Set a new maximum value for the Slider. The default value is 100.
   *
   * @param max the new maximum
   */
  void setMax(float max);

  /**
   * Get the current maximum value.
   *
   * @return current maximum
   */
  float getMax();

  /**
   * Set a new StepSize. This is the minimum value the Slider value can be changed.
   * The default value is 1.0f.
   *
   * @param stepSize the new step size
   */
  void setStepSize(float stepSize);

  /**
   * Get the current StepSize.
   *
   * @return current stepSize
   */
  float getStepSize();

  /**
   * Set a new Button StepSize. This is the value that the slider value changes when one of
   * the up/down or left/right button is clicked. The default value is 25.0f.
   *
   * @param buttonStepSize the new button step size value
   */
  void setButtonStepSize(float buttonStepSize);

  /**
   * Get the current button step size.
   *
   * @return the current step size
   */
  float getButtonStepSize();
}
