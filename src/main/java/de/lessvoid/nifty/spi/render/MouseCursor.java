package de.lessvoid.nifty.spi.render;

import de.lessvoid.nifty.NiftyMouse;

/**
 * A MouseCursor Handle ready to be applied as the current mouse cursor of the window.
 * This represents a loaded MouseCursor resource ready to be applied using {@link NiftyMouse}.
 * @author void
 */
public interface MouseCursor {

  /**
   * Dispose all resources this mouse cursor might require. After this has been called
   * the MouseCursor should not be used anymore.
   */
  void dispose();
}
