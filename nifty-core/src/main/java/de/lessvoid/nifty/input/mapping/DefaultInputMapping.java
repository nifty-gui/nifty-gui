package de.lessvoid.nifty.input.mapping;

import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * Default.
 * @author void
 */
public class DefaultInputMapping implements NiftyInputMapping {

  /**
   * convert the given KeyboardInputEvent into a neutralized NiftyInputEvent.
   * @param inputEvent input event
   * @return NiftInputEvent
   */
  public NiftyStandardInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_F1) {
        return NiftyStandardInputEvent.ConsoleToggle;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN) {
        return NiftyStandardInputEvent.Activate;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_SPACE) {
        return NiftyStandardInputEvent.Activate;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_TAB) {
        if (inputEvent.isShiftDown()) {
          return NiftyStandardInputEvent.PrevInputElement;
        } else {
          return NiftyStandardInputEvent.NextInputElement;
        }
      }
    }
    return null;
  }
}
