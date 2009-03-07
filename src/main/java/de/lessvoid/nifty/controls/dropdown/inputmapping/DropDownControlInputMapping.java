package de.lessvoid.nifty.controls.dropdown.inputmapping;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * StandardDropDown Keyboard Mapping for Nifty.
 * @author void
 */
public class DropDownControlInputMapping implements NiftyInputMapping {

  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == Keyboard.KEY_DOWN) {
        return NiftyInputEvent.MoveCursorDown;
      } else if (inputEvent.getKey() == Keyboard.KEY_UP) {
        return NiftyInputEvent.MoveCursorUp;
      } else if (inputEvent.getKey() == Keyboard.KEY_RETURN) {
        return NiftyInputEvent.Activate;
      } else if (inputEvent.getKey() == Keyboard.KEY_SPACE) {
        return NiftyInputEvent.Activate;
      } else if (inputEvent.getKey() == Keyboard.KEY_TAB) {
        if (inputEvent.isShiftDown()) {
          return NiftyInputEvent.PrevInputElement;
        } else {
          return NiftyInputEvent.NextInputElement;
        }
      }
    }
    return null;
  }
}
