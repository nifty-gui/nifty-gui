package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a Scrollbar value has been changed.
 * @author void
 */
public class ScrollbarChangedEvent implements NiftyEvent<Void> {
  private float value;

  public ScrollbarChangedEvent(final float newValue) {
    this.value = newValue;
  }

  public float getValue() {
    return value;
  }
}
