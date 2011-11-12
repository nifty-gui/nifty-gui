package de.lessvoid.nifty.controls;

/**
 * This is a Nifty Window that can be dragged around.
 * @author void
 */
public interface Window extends NiftyControl {
  /**
   * Get the title of the Window.
   * @return title the title of the window
   */
  String getTitle();

  /**
   * Set the title of the Window.
   * @param title the new title to be displayed in the Title bar
   */
  void setTitle(String title);

  /**
   * Close this Window.
   */
  void closeWindow();
}
