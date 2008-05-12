package de.lessvoid.nifty.input;

/**
 * NiftyInputMapping.
 * @author void
 */
public interface NiftyInputMapping {

  /**
   * convert the given lwjgl keyboard input data into a NiftyInputEvent.
   * @param eventKey key
   * @param eventCharacter character
   * @param keyDown down
   * @return NiftInputEvent
   */
  NiftyInputEvent convert(final int eventKey, final char eventCharacter, final boolean keyDown);
}
