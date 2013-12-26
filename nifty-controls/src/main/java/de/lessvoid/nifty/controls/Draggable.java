package de.lessvoid.nifty.controls;

import javax.annotation.Nullable;

/**
 * The Draggable Control interface.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface Draggable extends NiftyControl {
  /**
   * This function is used to disable the draggable properly.
   *
   * @param cancelCurrentDrag in case this parameter is set {@code true} the current dragging operation will be
   *                          canceled once this function is called.
   */
  void disable(boolean cancelCurrentDrag);

  /**
   * Move the draggable control to the front inside its parent element.
   */
  void moveToFront();

  /**
   * Set the droppable that is parent to this draggable. This could be needed in case the draggable was moved around
   * in the GUI by the application and not by the user.
   *
   * @param droppable the new parent droppable
   */
  void setDroppable(@Nullable Droppable droppable);
}
