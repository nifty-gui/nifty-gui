package de.lessvoid.nifty.controls.scrollbar;

import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

public class ScrollbarInputMapping implements NiftyInputMapping {

  @Override
  public NiftyStandardInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_DOWN) {
        return NiftyStandardInputEvent.MoveCursorDown;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
        return NiftyStandardInputEvent.MoveCursorUp;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
        return NiftyStandardInputEvent.MoveCursorPageDown;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
        return NiftyStandardInputEvent.MoveCursorPageUp;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_LEFT) {
          return NiftyStandardInputEvent.MoveCursorLeft;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RIGHT) {
          return NiftyStandardInputEvent.MoveCursorRight;
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
