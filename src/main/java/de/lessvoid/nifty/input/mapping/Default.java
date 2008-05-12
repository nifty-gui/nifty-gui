package de.lessvoid.nifty.input.mapping;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;

/**
 * Default.
 * @author void
 */
public class Default implements NiftyInputMapping {

  /**
   * convert.
   * @param eventKey eventKey
   * @param eventCharacter event character
   * @param keyDown key down
   * @return NiftyInputEvent
   */
  public NiftyInputEvent convert(
      final int eventKey,
      final char eventCharacter,
      final boolean keyDown) {
    if (keyDown) {
      if (eventKey == Keyboard.KEY_DOWN) {
        return NiftyInputEvent.NextInputElement;
      } else if (eventKey == Keyboard.KEY_UP) {
        return NiftyInputEvent.PrevInputElement;
      } else if (eventKey == Keyboard.KEY_RETURN) {
        return NiftyInputEvent.SubmitText;
      }
    }
    return null;
  }
}
