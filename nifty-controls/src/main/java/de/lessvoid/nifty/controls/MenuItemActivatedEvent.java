package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a menu item has been activated. The item
 * that was activated is being transmitted in this event.
 * @author void
 */
public class MenuItemActivatedEvent<T> implements NiftyEvent<T> {
  private Menu<T> menu;
  private T item;

  public MenuItemActivatedEvent(final Menu<T> menu, final T item) {
    this.menu = menu;
    this.item = item;
  }

  public Menu<T> getMenu() {
    return menu;
  }

  public T getItem() {
    return item;
  }
}
