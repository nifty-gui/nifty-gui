package de.lessvoid.nifty.input;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

import javax.annotation.Nullable;

/**
 * NiftyInputMapping.
 *
 * @author void
 */
public interface NiftyInputMapping {

  /**
   * convert the given KeyboardInputEvent into a neutralized NiftyInputEvent.
   *
   * @param inputEvent input event
   * @return NiftInputEvent
   */
  @Nullable
  NiftyInputEvent convert(KeyboardInputEvent inputEvent);
}
