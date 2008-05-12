package de.lessvoid.nifty.input.mapping;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;

/**
 * NiftyInputMapping that doesn't map at all.
 * @author void
 */
public class NoInputMapping implements NiftyInputMapping {

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
    return null;
  }
}
