package de.lessvoid.nifty.controls.scrollbar;

/**
 * The ScrollbarView is used to update the visual representation of the scrollbar.
 * @author void
 */
public interface ScrollbarView {

  /**
   * Get the size of the Slider area. This is the area in pixel that is available to
   * scroll around.
   * @return pixel size value
   */
  int getSize();

  /**
   * Set the position and the size of the handle.
   * @param pos position
   * @param size size
   */
  void setHandle(int pos, int size);

  /**
   * That's a callback that is called when the value has been changed.
   * @param value the new value
   */
  void valueChanged(float value);

  /**
   * Filter the correct value for the given view (horizontal or vertical) from the given mouse coordinates.
   * @param pixelX x position
   * @param pixelY y position
   * @return x or y specific to implementation
   */
  int filter(int pixelX, int pixelY);
}
