package de.lessvoid.nifty.controls;

/**
 * The Draggable Control interface.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface Draggable extends NiftyControl {
  /**
   * Move the draggable control to the front inside its parent element.
   */
  void moveToFront();
}
