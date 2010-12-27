package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;

/**
 * This event is generated when a NiftyInputEvent is received and send to any control.
 * @author void
 */
public class NiftyInputControlEvent implements NiftyEvent<Void> {
  private NiftyInputEvent inputEvent;

  public NiftyInputControlEvent(final NiftyInputEvent inputEvent) {
    this.inputEvent = inputEvent;
  }

  public NiftyInputEvent getInputEvent() {
    return inputEvent;
  }
}
