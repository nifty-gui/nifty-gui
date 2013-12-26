package de.lessvoid.nifty;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * This gives you access to the mouse from within Nifty. Its main purpose is
 * to change the shape of the current mouse pointer.
 *
 * @author void
 */
public interface NiftyMouse {

  /**
   * Register, load and prepare the given file for use as a mouse cursor later.
   *
   * @param id       id of mouse cursor for later reference
   * @param filename the image to load.
   * @param hotspotX the hotspot x coordinate of the cursor image with 0 being left
   * @param hotspotY the hotspot y coordinate of the cursor image with 0 being top
   * @return the MouseCursor handle
   */
  void registerMouseCursor(String id, String filename, int hotspotX, int hotspotY) throws IOException;

  /**
   * Get the current mouse cursor id or null if no mouse cursor is set.
   *
   * @return mouse cursor id or null
   */
  @Nullable
  String getCurrentId();

  /**
   * This unregisters all mouse cursors and disposes all resources that might have been allocated.
   */
  void unregisterAll();

  /**
   * Reset the mouse cursor back to the native one. This disables a currently
   * enabled custom mouse cursor.
   */
  void resetMouseCursor();

  /**
   * Enable the given MouseCursor.
   *
   * @param id the cursor id to enable
   */
  void enableMouseCursor(String id);

  /**
   * Set the mouse position to the given x, y coordinate with (0,0) being the upper
   * left corner of the screen.
   *
   * @param x x coordinate
   * @param y y coordinage
   */
  void setMousePosition(int x, int y);

  /**
   * Get the current mouse position x coordinate.
   *
   * @return x mouse x coordinate
   */
  int getX();

  /**
   * Get the current mouse position y coordinate.
   *
   * @return y mouse y coordinate
   */
  int getY();

  /**
   * This returns the time in ms that no mouse movement has occurred.
   *
   * @return the time in ms between now and the last time the mouse have been moved
   */
  long getNoMouseMovementTime();
}
