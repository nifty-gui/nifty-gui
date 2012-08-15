package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.input.NiftyStandardInputEvent;
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
   * Convert a keyboard input event to a {@link NiftyStandardInputEvent}.
   *
   * @param inputEvent the keyboard input event that needs to be converted
   * @return the {@link NiftyStandardInputEvent} that is assigned to the keyboard event or {@code null} in case no event is
   *         assigned
   */
  @Override
  public NiftyStandardInputEvent convert(final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      return handleKeyDownEvent(inputEvent);
    } else {
      return handleKeyUpEvent(inputEvent);
    }
  }

  /**
   * Translate a keyboard down event to a {@link NiftyStandardInputEvent} regarding the button that was used at this event.
   *
   * @param inputEvent the keyboard input event that needs translation
   * @return {@link NiftyStandardInputEvent} that is assigned to the keyboard event or {@code null} in case no event is
   *         assigned
   */
  private static NiftyStandardInputEvent handleKeyDownEvent(final KeyboardInputEvent inputEvent) {
    switch (inputEvent.getKey()) {
      case KeyboardInputEvent.KEY_UP:
        return NiftyStandardInputEvent.MoveCursorUp;
      case KeyboardInputEvent.KEY_DOWN:
        return NiftyStandardInputEvent.MoveCursorDown;
      case KeyboardInputEvent.KEY_LEFT:
        return NiftyStandardInputEvent.MoveCursorLeft;
      case KeyboardInputEvent.KEY_RIGHT:
        return NiftyStandardInputEvent.MoveCursorRight;
      case KeyboardInputEvent.KEY_F1:
        return NiftyStandardInputEvent.ConsoleToggle;
      case KeyboardInputEvent.KEY_RETURN:
        return NiftyStandardInputEvent.SubmitText;
      case KeyboardInputEvent.KEY_DELETE:
        return NiftyStandardInputEvent.Delete;
      case KeyboardInputEvent.KEY_BACK:
        return NiftyStandardInputEvent.Backspace;
      case KeyboardInputEvent.KEY_END:
        return NiftyStandardInputEvent.MoveCursorToLastPosition;
      case KeyboardInputEvent.KEY_HOME:
        return NiftyStandardInputEvent.MoveCursorToFirstPosition;
      case KeyboardInputEvent.KEY_LSHIFT:
      case KeyboardInputEvent.KEY_RSHIFT:
        return NiftyStandardInputEvent.SelectionStart;
      case KeyboardInputEvent.KEY_TAB:
        return inputEvent.isShiftDown() ? NiftyStandardInputEvent.PrevInputElement : NiftyStandardInputEvent.NextInputElement;
      case KeyboardInputEvent.KEY_X:
        if (inputEvent.isControlDown()) {
          return NiftyStandardInputEvent.Cut;
        }
        break;
      case KeyboardInputEvent.KEY_C:
        if (inputEvent.isControlDown()) {
          return NiftyStandardInputEvent.Copy;
        }
        break;
      case KeyboardInputEvent.KEY_V:
        if (inputEvent.isControlDown()) {
          return NiftyStandardInputEvent.Paste;
        }
        break;
      case KeyboardInputEvent.KEY_A:
        if (inputEvent.isControlDown()) {
          return NiftyStandardInputEvent.SelectAll;
        }
        break;
      default:
        break;
    }

    if (!Character.isISOControl(inputEvent.getCharacter())) {
      final NiftyStandardInputEvent character = NiftyStandardInputEvent.Character;
      character.setCharacter(inputEvent.getCharacter());
      return character;
    }
    return null;
  }

  /**
   * Translate a keyboard key released event into the assigned {@link NiftyStandardInputEvent}.
   *
   * @param inputEvent the keyboard input event that triggered the call of this function
   * @return the assigned {@link NiftyStandardInputEvent} or {@code null} in case no event is assigned
   */
  private static NiftyStandardInputEvent handleKeyUpEvent(final KeyboardInputEvent inputEvent) {
    switch (inputEvent.getKey()) {
      case KeyboardInputEvent.KEY_LSHIFT:
      case KeyboardInputEvent.KEY_RSHIFT:
        return NiftyStandardInputEvent.SelectionEnd;
      case KeyboardInputEvent.KEY_ESCAPE:
        return NiftyStandardInputEvent.Escape;
      default:
        return null;
    }
  }
}
