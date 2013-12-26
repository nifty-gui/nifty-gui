package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when a Scrollbar value has been changed.
 *
 * @author void
 */
public class ScrollbarChangedEvent implements NiftyEvent {
  @Nonnull
  private final Scrollbar scrollbar;
  private final float value;

  public ScrollbarChangedEvent(@Nonnull final Scrollbar scrollbar, final float newValue) {
    this.scrollbar = scrollbar;
    this.value = newValue;
  }

  @Nonnull
  public Scrollbar getScrollbar() {
    return scrollbar;
  }

  public float getValue() {
    return value;
  }
}
