package de.lessvoid.nifty.spi.input;

import java.util.List;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;

/**
 * Interface for InputSystem for Nifty.
 * @author void
 */
public interface InputSystem {

  /**
   * Get all available MouseEvents into a List.
   * @return List of MouseInputEvent for Nifty to process.
   */
  List < MouseInputEvent > getMouseEvents();

  /**
   * Get all available KeyboardInputEvents into a List.
   * @return List of KeyboardInputEvent for Nifty to process.
   */
  List < KeyboardInputEvent > getKeyboardEvents();
}
