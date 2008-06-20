package de.lessvoid.nifty.input;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * NiftyInputMapping.
 * @author void
 */
public interface NiftyInputMapping {

  /**
   * convert the given KeyboardInputEvent into a neutralized NiftyInputEvent.
   * @param inputEvent input event
   * @return NiftInputEvent
   */
  NiftyInputEvent convert(KeyboardInputEvent inputEvent);
}
