package de.lessvoid.nifty.controls.listbox;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * ListBoxSelectionMode is a selection mode for Nifty ListBox controls. Different
 * selection policies exist which will implement the {@link ListBoxSelectionMode} interface.
 *
 * @param <T>
 * @author void
 */
interface ListBoxSelectionMode<T> {

  /**
   * Clear the selection.
   */
  void clear();

  /**
   * Get the selection.
   *
   * @return list of items
   */
  @Nonnull
  List<T> getSelection();

  /**
   * Add a new element to the selection.
   */
  void add(@Nonnull T item);

  /**
   * Remove the given element from the selection.
   */
  void remove(@Nonnull T item);

  /**
   * Remove the given element from the selection. This will remove the item even when requiresSelection is turned on.
   * It's called when the actual item gets removed and therefore we can't keep the selection.
   */
  void removeForced(@Nonnull T item);

  /**
   * When this is set to true the selection can't be completely de selected. There has
   * to be at least a single selection.
   *
   * @param enable true when the requires selection mode should be enabled
   */
  void enableRequiresSelection(boolean enable);

  /**
   * Returns if this Selection Mode requires a selection.
   *
   * @return true this selection mode requires a selection
   */
  boolean requiresAutoSelection();
}
