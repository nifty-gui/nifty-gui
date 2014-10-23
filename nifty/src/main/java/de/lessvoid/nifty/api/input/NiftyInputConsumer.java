package de.lessvoid.nifty.api.input;



/**
 * NiftyInputConsumer consumes InputEvents from the InputSystem.
 * @author void
 */
public interface NiftyInputConsumer {

  /**
   * Send the given mouse event to Nifty for processing.
   * @param mouseX the mouse x position when the event occurred
   * @param mouseY the mouse y position when the event occurred
   * @param mouseWheel the mouse wheel event
   * @param button the button that has been pressed with -1 = no button, 0 = first button, 1 = second button and so on
   * @param buttonDown the button was pressed down (true) or has been released (false)
   * @return true this event has been handled by nifty and false when not (in the later case this event should be processed by the caller)
   */
  boolean processPointerEvent(NiftyPointerEvent ... pointerEvents);

  /**
   * Process the given keyboard event.
   * @param keyEvent the keyboard event to process
   * @return true, when the keyboard event has been processed and false, if not
   */
  boolean processKeyboardEvent(NiftyKeyboardEvent keyEvent);
}
