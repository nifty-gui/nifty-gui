package de.lessvoid.nifty.controls;

import java.util.List;

/**
 * ListBoxSelectionMode is a selection mode for Nifty ListBox controls. Different
 * selection policies exist which will implement the {@link ListBoxSelectionMode} interface.
 * @author void
 * @param <T>
 */
public interface ListBoxSelectionMode<T> {

  /**
   * Clear the selection.
   */
  void clear();

  /**
   * Get the selection.
   * @return list of items
   */
  List<T> getSelection();

  /**
   * Add a new element to the selection.
   * @param item
   */
  void add(T item);

  /**
   * Remove the given element from the selection.
   * @param item
   */
  void remove(T item);
}
