package de.lessvoid.nifty.controls.imageSelect;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * ImageSelectInputMapping.
 * @author void
 */
public class ImageSelectInputMapping implements NiftyInputMapping {

  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_LEFT) {
        return NiftyInputEvent.MoveCursorLeft;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RIGHT) {
        return NiftyInputEvent.MoveCursorRight;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN) {
        return NiftyInputEvent.Activate;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_SPACE) {
        return NiftyInputEvent.Activate;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_TAB) {
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
