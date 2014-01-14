package de.lessvoid.nifty.spi.render;

/**
 * A MouseCursor Handle ready to be applied as the current mouse cursor of the window.
 *
 * @author void
 */
public interface MouseCursor {
  /**
   * Enable (show) this mouse cursor, hiding / replacing the existing cursor.
   */
  void enable();

  /**
   * Disable (hide) this mouse cursor, showing / restoring the existing cursor.
   */
  void disable();

  /**
   * Dispose all resources this mouse cursor might require. After this has been called the MouseCursor should not be
   * used anymore.
   */
  void dispose();
}
