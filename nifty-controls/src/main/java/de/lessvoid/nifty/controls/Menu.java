package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Menu interface is the Nifty control API view of a Menu control.
 *
 * @param <T>
 * @author void
 */
public interface Menu<T> extends NiftyControl {

  /**
   * Add a menu item to this Menu.
   *
   * @param menuText the text to display
   * @param item     the item
   */
  void addMenuItem(@Nonnull String menuText, @Nonnull T item);

  /**
   * Add a menu item to this Menu.
   *
   * @param menuText the text to display
   * @param menuIcon the icon (image) to display
   * @param item     the item
   */
  void addMenuItem(@Nonnull String menuText, @Nullable String menuIcon, @Nonnull T item);

  /**
   * Add a separator.
   */
  void addMenuItemSeparator();

}