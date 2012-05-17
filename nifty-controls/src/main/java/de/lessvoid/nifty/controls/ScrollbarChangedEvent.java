package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a Scrollbar value has been changed.
 * @author void
 */
public class ScrollbarChangedEvent implements NiftyEvent {
  private Scrollbar scrollbar;
  private float value;

  public ScrollbarChangedEvent(final Scrollbar scrollbar, final float newValue) {
    this.scrollbar = scrollbar;
    this.value = newValue;
  }

  public Scrollbar getScrollbar() {
    return scrollbar;
  }

  public float getValue() {
    return value;
  }
}
