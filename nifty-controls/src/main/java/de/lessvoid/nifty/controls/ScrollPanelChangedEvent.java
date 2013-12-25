package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when a ScrollPanel position has changed.
 *
 * @author void
 */
public class ScrollPanelChangedEvent implements NiftyEvent {
  @Nonnull
  private final ScrollPanel scrollPanel;
  private final float x;
  private final float y;

  public ScrollPanelChangedEvent(@Nonnull final ScrollPanel scrollPanel, final float newX, final float newY) {
    this.scrollPanel = scrollPanel;
    this.x = newX;
    this.y = newY;
  }

  @Nonnull
  public ScrollPanel getScrollPanel() {
    return scrollPanel;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }
}
