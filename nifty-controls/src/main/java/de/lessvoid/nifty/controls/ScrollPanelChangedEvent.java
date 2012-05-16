package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a ScrollPanel position has changed.
 * @author void
 */
public class ScrollPanelChangedEvent implements NiftyEvent {
  private ScrollPanel scrollPanel;
  private float x;
  private float y;

  public ScrollPanelChangedEvent(final ScrollPanel scrollPanel, final float newX, final float newY) {
    this.scrollPanel = scrollPanel;
    this.x = newX;
    this.y = newY;
  }

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
