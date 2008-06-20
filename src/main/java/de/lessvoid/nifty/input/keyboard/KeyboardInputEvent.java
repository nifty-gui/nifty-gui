package de.lessvoid.nifty.input.keyboard;

/**
 * A Keyboard Input Event.
 * @author void
 */
public class KeyboardInputEvent {
  /**
   * key.
   */
  private int key;

  /**
   * character.
   */
  private char character;

  /**
   * keyDown.
   */
  private boolean keyDown;

  /**
   * shiftDown.
   */
  private boolean shiftDown;

  /**
   * controlDown.
   */
  private boolean controlDown;

  /**
   * create the event.
   * @param newKey key
   * @param newCharacter character
   * @param newKeyDown keyDown
   * @param newShiftDown shiftDown
   * @param newControlDown controlDown
   */
  public KeyboardInputEvent(
      final int newKey,
      final char newCharacter,
      final boolean newKeyDown,
      final boolean newShiftDown,
      final boolean newControlDown) {
    this.key = newKey;
    this.character = newCharacter;
    this.keyDown = newKeyDown;
    this.shiftDown = newShiftDown;
    this.controlDown = newControlDown;
  }

  /**
   * @return the key
   */
  public int getKey() {
    return key;
  }

  /**
   * @return the character
   */
  public char getCharacter() {
    return character;
  }

  /**
   * @return the keyDown
   */
  public boolean isKeyDown() {
    return keyDown;
  }

  /**
   * @return the shiftDown
   */
  public boolean isShiftDown() {
    return shiftDown;
  }

  /**
   * @return the controlDown
   */
  public boolean isControlDown() {
    return controlDown;
  }
}
