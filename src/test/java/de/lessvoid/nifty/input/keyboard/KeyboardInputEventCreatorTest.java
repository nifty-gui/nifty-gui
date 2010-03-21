package de.lessvoid.nifty.input.keyboard;

import org.lwjgl.input.Keyboard;

import junit.framework.TestCase;

/**
 * KeyboardInputEventCreatorTest.
 * @author void
 */
public class KeyboardInputEventCreatorTest extends TestCase {
  /**
   * creator.
   */
  private KeyboardInputEventCreator creator;

  /**
   * setUp.
   */
  public void setUp() {
    this.creator = new KeyboardInputEventCreator();
  }

  /**
   * testBasicCreate.
   */
  public void testBasicCreate() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_0, '0', true);
    assertEvent(event, Keyboard.KEY_0, '0', true, false, false);
  }

  /**
   * testShiftDown.
   */
  public void testShiftDown() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_LSHIFT, ' ', true);
    assertEvent(event, Keyboard.KEY_LSHIFT, ' ', true, true, false);
    event = this.creator.createEvent(Keyboard.KEY_RSHIFT, ' ', true);
    assertEvent(event, Keyboard.KEY_RSHIFT, ' ', true, true, false);
  }

  /**
   * testShiftUp.
   */
  public void testShiftUp() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_LSHIFT, ' ', false);
    assertEvent(event, Keyboard.KEY_LSHIFT, ' ', false, false, false);
    event = this.creator.createEvent(Keyboard.KEY_RSHIFT, ' ', false);
    assertEvent(event, Keyboard.KEY_RSHIFT, ' ', false, false, false);
  }

  /**
   * testShiftDownKeySequence.
   */
  public void testShiftDownKeySequence() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_LSHIFT, ' ', true);
    assertEvent(event, Keyboard.KEY_LSHIFT, ' ', true, true, false);
    event = this.creator.createEvent(Keyboard.KEY_A, 'a', true);
    assertEvent(event, Keyboard.KEY_A, 'a', true, true, false);
    event = this.creator.createEvent(Keyboard.KEY_LSHIFT, ' ', false);
    assertEvent(event, Keyboard.KEY_LSHIFT, ' ', false, false, false);
    event = this.creator.createEvent(Keyboard.KEY_A, 'a', true);
    assertEvent(event, Keyboard.KEY_A, 'a', true, false, false);
  }

  /**
   * testControlDown.
   */
  public void testControlDown() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_LCONTROL, ' ', true);
    assertEvent(event, Keyboard.KEY_LCONTROL, (char)0, true, false, true);
    event = this.creator.createEvent(Keyboard.KEY_RCONTROL, ' ', true);
    assertEvent(event, Keyboard.KEY_RCONTROL, (char)0, true, false, true);
  }

  /**
   * testControlUp.
   */
  public void testControlUp() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_LCONTROL, ' ', false);
    assertEvent(event, Keyboard.KEY_LCONTROL, ' ', false, false, false);
    event = this.creator.createEvent(Keyboard.KEY_RCONTROL, ' ', false);
    assertEvent(event, Keyboard.KEY_RCONTROL, ' ', false, false, false);
  }

  /**
   * testControlDownKeySequence.
   */
  public void testControlDownKeySequence() {
    KeyboardInputEvent event = this.creator.createEvent(Keyboard.KEY_LCONTROL, ' ', true);
    assertEvent(event, Keyboard.KEY_LCONTROL, (char)0, true, false, true);
    event = this.creator.createEvent(Keyboard.KEY_A, 'a', true);
    assertEvent(event, Keyboard.KEY_A, (char)0, true, false, true);
    event = this.creator.createEvent(Keyboard.KEY_LCONTROL, ' ', false);
    assertEvent(event, Keyboard.KEY_LCONTROL, ' ', false, false, false);
    event = this.creator.createEvent(Keyboard.KEY_A, 'a', true);
    assertEvent(event, Keyboard.KEY_A, 'a', true, false, false);
  }

  /**
   * assert the event.
   * @param event event
   * @param expectedKey key
   * @param expectedChar char
   * @param expectedKeyDown expectedKeyDown
   * @param expectedShiftDown expectedShiftDown
   * @param expectedControlDown expectedControlDown
   */
  private void assertEvent(
      final KeyboardInputEvent event,
      final int expectedKey,
      final char expectedChar,
      final boolean expectedKeyDown,
      final boolean expectedShiftDown,
      final boolean expectedControlDown) {
    assertEquals(expectedKey, event.getKey());
    assertEquals(expectedChar, event.getCharacter());
    assertEquals(expectedKeyDown, event.isKeyDown());
    assertEquals(expectedShiftDown, event.isShiftDown());
    assertEquals(expectedControlDown, event.isControlDown());
  }
}
