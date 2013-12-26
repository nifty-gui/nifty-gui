package de.lessvoid.nifty.controls.scrollbar;

/**
 * The ScrollbarView is used to update the visual representation of the scrollbar.
 *
 * @author void
 */
public interface ScrollbarView {

  /**
   * Get the size of the Scrollbar area. This is the area in pixel that is available to
   * scroll around.
   *
   * @return pixel size value
   */
  int getAreaSize();

  /**
   * Returns the minimum size this View can show the handle without distortion. The handle
   * will never be smaller than this value.
   *
   * @return minimum handle size
   */
  int getMinHandleSize();

  /**
   * Set the position and the size of the handle.
   *
   * @param pos  position
   * @param size size
   */
  void setHandle(int pos, int size);

  /**
   * That's a callback that is called when the value has been changed.
   *
   * @param value the new value
   */
  void valueChanged(float value);

  /**
   * Filter the correct value for the given view (horizontal or vertical) from the given mouse coordinates.
   *
   * @param pixelX x position
   * @param pixelY y position
   * @return x or y specific to implementation
   */
  int filter(int pixelX, int pixelY);
}
