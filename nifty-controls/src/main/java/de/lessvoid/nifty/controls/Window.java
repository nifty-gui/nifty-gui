package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is a Nifty Window that can be dragged around.
 *
 * @author void
 */
public interface Window extends NiftyControl {
  /**
   * Get the title of the Window.
   *
   * @return title the title of the window
   */
  @Nullable
  String getTitle();

  /**
   * Set the title of the Window.
   *
   * @param title the new title to be displayed in the Title bar
   */
  void setTitle(@Nonnull String title);

  /**
   * Close this Window.
   */
  void closeWindow();

  /**
   * Move the window control to the front inside its parent element.
   */
  void moveToFront();
}
