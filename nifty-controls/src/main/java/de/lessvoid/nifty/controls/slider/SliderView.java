package de.lessvoid.nifty.controls.slider;

/**
 * The SliderView is used to update a sliders visual representation.
 *
 * @author void
 */
public interface SliderView {

  /**
   * Get the size of the Slider area. This is the area in pixel that is available to
   * scroll around.
   *
   * @return pixel size value
   */
  int getSize();

  /**
   * Update the Position of the slider.
   *
   * @param position the new position in px
   */
  void update(int position);

  /**
   * This translates the given x/y position into a single value. A horizontal slider will simply
   * return x and a vertical one will return y.
   *
   * @param pixelX x
   * @param pixelY y
   * @return value
   */
  int filter(int pixelX, int pixelY);

  /**
   * That's a callback that is called when the value has been changed.
   *
   * @param value the new value
   */
  void valueChanged(float value);
}
