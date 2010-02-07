package de.lessvoid.nifty.controls.textfield.inputmapping;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * StandardTextField InputMapping.
 * @author void
 */
public class TextFieldInputMapping implements NiftyInputMapping {

  /**
   * convert the given KeyboardInputEvent into a neutralized NiftyInputEvent.
   * @param inputEvent input event
   * @return NiftInputEvent
   */
  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      return handleKeyDownEvent(inputEvent);
    } else {
      return handleKeyUpEvent(inputEvent);
    }
  }

  /**
   * handle key down event.
   * @param inputEvent inputEvent
   * @return NiftyInputEvent
   */
  private NiftyInputEvent handleKeyDownEvent(final KeyboardInputEvent inputEvent) {
    if (inputEvent.getKey() == Keyboard.KEY_LEFT) {
      return NiftyInputEvent.MoveCursorLeft;
    } else if (inputEvent.getKey() == Keyboard.KEY_F1) {
      return NiftyInputEvent.ConsoleToggle;
    } else if (inputEvent.getKey() == Keyboard.KEY_RIGHT) {
      return NiftyInputEvent.MoveCursorRight;
    } else if (inputEvent.getKey() == Keyboard.KEY_RETURN) {
      return NiftyInputEvent.SubmitText;
    } else if (inputEvent.getKey() == Keyboard.KEY_DELETE) {
      return NiftyInputEvent.Delete;
    } else if (inputEvent.getKey() == Keyboard.KEY_BACK) {
      return NiftyInputEvent.Backspace;
    } else if (inputEvent.getKey() == Keyboard.KEY_END) {
      return NiftyInputEvent.MoveCursorToLastPosition;
    } else if (inputEvent.getKey() == Keyboard.KEY_HOME) {
      return NiftyInputEvent.MoveCursorToFirstPosition;
    } else if (inputEvent.getKey() == Keyboard.KEY_LSHIFT || inputEvent.getKey() == Keyboard.KEY_RSHIFT) {
      return NiftyInputEvent.SelectionStart;
    } else if (inputEvent.getKey() == Keyboard.KEY_TAB) {
      if (inputEvent.isShiftDown()) {
        return NiftyInputEvent.PrevInputElement;
      } else {
        return NiftyInputEvent.NextInputElement;
      }
    } else if (!Character.isISOControl(inputEvent.getCharacter())) {
      NiftyInputEvent character = NiftyInputEvent.Character;
      character.setCharacter(inputEvent.getCharacter());
      return character;
    }

    if (inputEvent.isControlDown()) {
      if (inputEvent.getKey() == Keyboard.KEY_X) {
        return NiftyInputEvent.Cut;
      } else if (inputEvent.getKey() == Keyboard.KEY_C) {
        return NiftyInputEvent.Copy;
      } else if (inputEvent.getKey() == Keyboard.KEY_V) {
        return NiftyInputEvent.Paste;
      }
    }
    return null;
  }

  /**
   * handle key up event.
   * @param inputEvent inputEvent
   * @return NiftyInputEvent
   */
  private NiftyInputEvent handleKeyUpEvent(final KeyboardInputEvent inputEvent) {
    if (inputEvent.getKey() == Keyboard.KEY_LSHIFT || inputEvent.getKey() == Keyboard.KEY_RSHIFT) {
      return NiftyInputEvent.SelectionEnd;
    } else if (inputEvent.getKey() == Keyboard.KEY_ESCAPE) {
      return NiftyInputEvent.Escape;
    }
    return null;
  }
}
