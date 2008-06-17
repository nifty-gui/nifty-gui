package de.lessvoid.nifty.input.mapping;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;

/**
 * StandardTextField InputMapping.
 * @author void
 */
public class StandardTextField implements NiftyInputMapping {

  /**
   * min char constant.
   */
  private static final int MIN_CHAR = 31;

  /**
   * max char constant.
   */
  private static final int MAX_CHAR = 127;

  /**
   * remembers if the control key is pressed.
   */
  private boolean controlKeyPressed = false;

  /**
   * convert.
   * @param eventKey eventKey
   * @param eventCharacter eventCharacter
   * @param keyDown key down
   * @return NiftyInputEvent
   */
  public NiftyInputEvent convert(
      final int eventKey,
      final char eventCharacter,
      final boolean keyDown) {
    if (keyDown) {
      return handleKeyDownEvent(eventKey, eventCharacter, keyDown);
    } else {
      return handleKeyUpEvent(eventKey);
    }
  }

  /**
   * handle key up event.
   * @param eventKey event key
   * @return NiftyInputEvent
   */
  private NiftyInputEvent handleKeyUpEvent(final int eventKey) {
    if (eventKey == Keyboard.KEY_LCONTROL || eventKey == Keyboard.KEY_RCONTROL) {
      controlKeyPressed = false;
    } else if (eventKey == Keyboard.KEY_LSHIFT || eventKey == Keyboard.KEY_RSHIFT) {
      return NiftyInputEvent.SelectionEnd;
    }
    return null;
  }

  /**
   * handle key down event.
   * @param eventKey event key
   * @param eventCharacter event character
   * @param keyDown key down or key up
   * @return NiftyInputEvent
   */
  private NiftyInputEvent handleKeyDownEvent(
      final int eventKey,
      final char eventCharacter,
      final boolean keyDown) {
    if (eventKey == Keyboard.KEY_LEFT) {
      return NiftyInputEvent.MoveCursorLeft;
    } else if (eventKey == Keyboard.KEY_RIGHT) {
      return NiftyInputEvent.MoveCursorRight;
    } else if (eventKey == Keyboard.KEY_RETURN) {
      return NiftyInputEvent.SubmitText;
    } else if (eventKey == Keyboard.KEY_DELETE) {
      return NiftyInputEvent.Delete;
    } else if (eventKey == Keyboard.KEY_BACK) {
      return NiftyInputEvent.Backspace;
    } else if (eventKey == Keyboard.KEY_END) {
      return NiftyInputEvent.MoveCursorToLastPosition;
    } else if (eventKey == Keyboard.KEY_HOME) {
      return NiftyInputEvent.MoveCursorToFirstPosition;
    } else if (eventKey == Keyboard.KEY_LSHIFT || eventKey == Keyboard.KEY_RSHIFT) {
      return NiftyInputEvent.SelectionStart;
    } else if (eventKey == Keyboard.KEY_LCONTROL || eventKey == Keyboard.KEY_RCONTROL) {
      controlKeyPressed = true;
    } else if (eventCharacter > MIN_CHAR && eventCharacter < MAX_CHAR) {
      NiftyInputEvent character = NiftyInputEvent.Character;
      character.setCharacter(eventCharacter);
      return character;
    }

    if (controlKeyPressed) {
      if (eventKey == Keyboard.KEY_X) {
        return NiftyInputEvent.Cut;
      } else if (keyDown && (eventKey == Keyboard.KEY_C)) {
        return NiftyInputEvent.Copy;
      } else if (keyDown && (eventKey == Keyboard.KEY_V)) {
        return NiftyInputEvent.Paste;
      }
    }
    return null;
  }
}
