package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a menu item has been activated. The item
 * that was activated is being transmitted in this event.
 * @author void
 */
public class MenuItemActivatedEvent<T> implements NiftyEvent<T> {
  private T item;

  public MenuItemActivatedEvent(final T item) {
    this.item = item;
  }

  public T getItem() {
    return item;
  }
}
