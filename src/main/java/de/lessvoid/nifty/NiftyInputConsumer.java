package de.lessvoid.nifty;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;

/**
 * NiftyInputConsumer consumes InputEvents from the InputSystem.
 * @author void
 */
public interface NiftyInputConsumer {

  /**
   * Process the given mouse event.
   * @param mouseEvent the mouse event to process
   * @return true, when the mouse event has been processed and false, if not
   */
  boolean processMouseEvent(MouseInputEvent mouseEvent);

  /**
   * Process the given keyboard event.
   * @param keyEvent the keyboard event to process
   * @return true, when the mouse event has been processed and false, if not
   */
  boolean processKeyboardEvent(KeyboardInputEvent keyEvent);
}
