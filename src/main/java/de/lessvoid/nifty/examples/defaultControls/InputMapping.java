package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

public class InputMapping implements NiftyInputMapping {

  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_RIGHT) {
        return NiftyInputEvent.MoveCursorRight;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_LEFT) {
        return NiftyInputEvent.MoveCursorLeft;
      }
    }
    return null;
  }
}
