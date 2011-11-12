package de.lessvoid.nifty.renderer.lwjgl.input;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;


/**
 * KeyboardInputEventCreator.
 * @author void
 */
public class LwjglKeyboardInputEventCreator {

  /**
   * shiftDown.
   */
  private boolean shiftDown = false;

  /**
   * controlDown.
   */
  private boolean controlDown = false;

  /**
   * create KeyboardInputEvent.
   * @param key key
   * @param character character
   * @param keyDown keyDown
   * @return event
   */
  public KeyboardInputEvent createEvent(final int key, final char character, final boolean keyDown) {
    if (isShiftDown(key, keyDown)) {
      shiftDown = true;
    } else if (isShiftUp(key, keyDown)) {
      shiftDown = false;
    } else if (isControlDown(key, keyDown)) {
      controlDown = true;
    } else if (isControlUp(key, keyDown)) {
      controlDown = false;
    }
    // because Nifty uses the same keyboard encoding like lwjgl does, we can directly forward
    // the keyboard event to Nifty without the need for conversion
    return new KeyboardInputEvent(key, character, keyDown, shiftDown, controlDown);
  }

  /**
   * checks if the shift key is given.
   * @param key key
   * @return true when shift has been pressed and false otherwise
   */
  private boolean isShiftKey(final int key) {
    return key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_RSHIFT;
  }

  /**
   * check if shift is down.
   * @param key key to check
   * @param keyDown keyDown
   * @return true when left or right shift has been pressed
   */
  private boolean isShiftDown(final int key, final boolean keyDown) {
    return keyDown && isShiftKey(key);
  }

  /**
   * check if shift is up.
   * @param key key
   * @param keyDown keyDown
   * @return true when left or right shift has been released
   */
  private boolean isShiftUp(final int key, final boolean keyDown) {
    return !keyDown && isShiftKey(key);
  }

  /**
   * check if the given key is the controlKey.
   * @param key key
   * @return true left or right control key pressed and false otherwise
   */
  private boolean isControlKey(final int key) {
    return key == Keyboard.KEY_RCONTROL || key == Keyboard.KEY_LCONTROL || key == Keyboard.KEY_LMETA || key == Keyboard.KEY_RMETA;
  }

  /**
   * check if control key is down.
   * @param key key
   * @param keyDown keyDown
   * @return controlDown
   */
  private boolean isControlDown(final int key, final boolean keyDown) {
    return keyDown && isControlKey(key);
  }

  /**
   * check if control key is up.
   * @param key key
   * @param keyDown keyDown
   * @return controlDown
   */
  private boolean isControlUp(final int key, final boolean keyDown) {
    return !keyDown && isControlKey(key);
  }
}
