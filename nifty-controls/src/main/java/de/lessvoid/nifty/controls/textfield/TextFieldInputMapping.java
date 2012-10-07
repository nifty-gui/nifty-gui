package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * The input mapping for the {@link de.lessvoid.nifty.controls.TextField}.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TextFieldInputMapping implements NiftyInputMapping {
  /**
   * Convert a keyboard input event to a {@link NiftyInputEvent}.
   *
   * @param inputEvent the keyboard input event that needs to be converted
   * @return the {@link NiftyInputEvent} that is assigned to the keyboard event or {@code null} in case no event is
   *         assigned
   */
  @Override
  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      return handleKeyDownEvent(inputEvent);
    } else {
      return handleKeyUpEvent(inputEvent);
    }
  }

  /**
   * Translate a keyboard down event to a {@link NiftyInputEvent} regarding the button that was used at this event.
   *
   * @param inputEvent the keyboard input event that needs translation
   * @return {@link NiftyInputEvent} that is assigned to the keyboard event or {@code null} in case no event is
   *         assigned
   */
  private static NiftyInputEvent handleKeyDownEvent(final KeyboardInputEvent inputEvent) {
    switch (inputEvent.getKey()) {
      case KeyboardInputEvent.KEY_UP:
        return NiftyInputEvent.MoveCursorUp;
      case KeyboardInputEvent.KEY_DOWN:
        return NiftyInputEvent.MoveCursorDown;
      case KeyboardInputEvent.KEY_LEFT:
        return NiftyInputEvent.MoveCursorLeft;
      case KeyboardInputEvent.KEY_RIGHT:
        return NiftyInputEvent.MoveCursorRight;
      case KeyboardInputEvent.KEY_F1:
        return NiftyInputEvent.ConsoleToggle;
      case KeyboardInputEvent.KEY_RETURN:
        return NiftyInputEvent.SubmitText;
      case KeyboardInputEvent.KEY_DELETE:
        return NiftyInputEvent.Delete;
      case KeyboardInputEvent.KEY_BACK:
        return NiftyInputEvent.Backspace;
      case KeyboardInputEvent.KEY_END:
        return NiftyInputEvent.MoveCursorToLastPosition;
      case KeyboardInputEvent.KEY_HOME:
        return NiftyInputEvent.MoveCursorToFirstPosition;
      case KeyboardInputEvent.KEY_LSHIFT:
      case KeyboardInputEvent.KEY_RSHIFT:
        return NiftyInputEvent.SelectionStart;
      case KeyboardInputEvent.KEY_TAB:
        return inputEvent.isShiftDown() ? NiftyInputEvent.PrevInputElement : NiftyInputEvent.NextInputElement;
      case KeyboardInputEvent.KEY_X:
        if (inputEvent.isControlDown()) {
          return NiftyInputEvent.Cut;
        }
        break;
      case KeyboardInputEvent.KEY_C:
        if (inputEvent.isControlDown()) {
          return NiftyInputEvent.Copy;
        }
        break;
      case KeyboardInputEvent.KEY_V:
        if (inputEvent.isControlDown()) {
          return NiftyInputEvent.Paste;
        }
        break;
      case KeyboardInputEvent.KEY_A:
        if (inputEvent.isControlDown()) {
          return NiftyInputEvent.SelectAll;
        }
        break;
      default:
        break;
    }

    if (!Character.isISOControl(inputEvent.getCharacter())) {
      final NiftyInputEvent character = NiftyInputEvent.Character;
      character.setCharacter(inputEvent.getCharacter());
      return character;
    }
    return null;
  }

  /**
   * Translate a keyboard key released event into the assigned {@link NiftyInputEvent}.
   *
   * @param inputEvent the keyboard input event that triggered the call of this function
   * @return the assigned {@link NiftyInputEvent} or {@code null} in case no event is assigned
   */
  private static NiftyInputEvent handleKeyUpEvent(final KeyboardInputEvent inputEvent) {
    switch (inputEvent.getKey()) {
      case KeyboardInputEvent.KEY_LSHIFT:
      case KeyboardInputEvent.KEY_RSHIFT:
        return NiftyInputEvent.SelectionEnd;
      case KeyboardInputEvent.KEY_ESCAPE:
        return NiftyInputEvent.Escape;
      default:
        return null;
    }
  }
}
