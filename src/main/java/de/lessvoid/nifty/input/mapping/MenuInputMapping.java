package de.lessvoid.nifty.input.mapping;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

public class MenuInputMapping implements NiftyInputMapping {

  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == Keyboard.KEY_F1) {
        return NiftyInputEvent.ConsoleToggle;
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
      } else if (inputEvent.getKey() == Keyboard.KEY_UP) {
        return NiftyInputEvent.MoveCursorUp;
      } else if (inputEvent.getKey() == Keyboard.KEY_DOWN) {
        return NiftyInputEvent.MoveCursorDown;
      }
    }
    return null;
  }
}
