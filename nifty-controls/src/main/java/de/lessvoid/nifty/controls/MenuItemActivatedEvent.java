package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when a menu item has been activated. The item
 * that was activated is being transmitted in this event.
 *
 * @author void
 */
public class MenuItemActivatedEvent<T> implements NiftyEvent {
  @Nonnull
  private final Menu<T> menu;
  @Nonnull
  private final T item;

  public MenuItemActivatedEvent(@Nonnull final Menu<T> menu, @Nonnull final T item) {
    this.menu = menu;
    this.item = item;
  }

  @Nonnull
  public Menu<T> getMenu() {
    return menu;
  }

  @Nonnull
  public T getItem() {
    return item;
  }
}
