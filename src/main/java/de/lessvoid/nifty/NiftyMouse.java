package de.lessvoid.nifty;

import java.io.IOException;

import de.lessvoid.nifty.spi.render.MouseCursor;

/**
 * This gives you access to the mouse from within Nifty. Its main purpose is
 * to change the shape of the current mouse pointer.
 * @author void
 */
public interface NiftyMouse {

  /**
   * Register, load and prepare the given file for use as a mouse cursor later.
   * @param filename the image to load.
   * @param hotspotX the hotspot x coordinate of the cursor image with 0 being left
   * @param hotspotY the hotspot y coordinate of the cursor image with 0 being top
   * @return the MouseCursor handle
   */
  MouseCursor registerMouseCursor(String filename, int hotspotX, int hotspotY) throws IOException;

  /**
   * Reset the mouse cursor back to the native one. This disables a currently
   * enabled custom mouse cursor.
   */
  void resetMouseCursor();

  /**
   * Enable the given MouseCursor.
   * @param cursor the mouse cursor to enable.
   */
  void enableMouseCursor(MouseCursor cursor);

  /**
   * Set the mouse position to the given x, y coordinate with (0,0) being the upper
   * left corner of the screen.
   * @param x x coordinate
   * @param y y coordinage
   */
  void setMousePosition(int x, int y);
}
