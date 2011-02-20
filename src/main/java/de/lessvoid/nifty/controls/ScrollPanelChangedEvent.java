package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a ScrollPanel position has changed.
 * @author void
 */
public class ScrollPanelChangedEvent implements NiftyEvent<Void> {
  private float x;
  private float y;

  public ScrollPanelChangedEvent(final float newX, final float newY) {
    this.x = newX;
    this.y = newY;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }
}
