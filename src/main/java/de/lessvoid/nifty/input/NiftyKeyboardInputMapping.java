package de.lessvoid.nifty.input;

/**
 * NiftyInputMapping for Keyboard.
 * @author void
 */
public interface NiftyKeyboardInputMapping extends NiftyInputMapping {

  /**
   * Supposed to convert the given lwjgl keyboard event data into a
   * NiftyInputEvent instance.
   * @param eventKey eventKey
   * @param eventCharacter eventCharacter
   * @param keyDown keyDown
   * @return NiftyInputEvent
   */
  NiftyInputEvent map(int eventKey, char eventCharacter, boolean keyDown);
}
