package de.lessvoid.nifty;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * You can register an instance of this interface with Nifty and Nifty will notify you for every event it receives
 * and if Nifty actually handled it. This is probably only useful for debugging purpose tho.
 * 
 * @author void
 */
public interface NiftyInputConsumerNotify {
  void processedMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown, boolean processed);
  void processKeyboardEvent(KeyboardInputEvent keyEvent, boolean processed);
}
