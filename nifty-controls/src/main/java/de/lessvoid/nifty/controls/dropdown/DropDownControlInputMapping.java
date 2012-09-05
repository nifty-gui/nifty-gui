package de.lessvoid.nifty.controls.dropdown;



import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * StandardDropDown Keyboard Mapping for Nifty.
 * @author void
 */
public class DropDownControlInputMapping implements NiftyInputMapping {

  @Override
  public NiftyStandardInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_DOWN) {
        return NiftyStandardInputEvent.MoveCursorDown;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
        return NiftyStandardInputEvent.MoveCursorUp;
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
