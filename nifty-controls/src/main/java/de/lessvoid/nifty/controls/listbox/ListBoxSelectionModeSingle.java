package de.lessvoid.nifty.controls.listbox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * A single selection mode for a Nifty ListBox. You can only select a single item.
 * Selecting a new one will remove any previous selected items.
 *
 * @param <T>
 * @author void
 */
class ListBoxSelectionModeSingle<T> implements ListBoxSelectionMode<T> {
  @Nullable
  private T selection;
  private boolean requiresSelection = false;

  @Override
  public void clear() {
    selection = null;
  }

  @Nonnull
  @Override
  public List<T> getSelection() {
    if (selection == null) {
      return Collections.emptyList();
    }
    return Collections.singletonList(selection);
  }

  @Override
  public void remove(@Nonnull final T item) {
    if (requiresSelection) {
      return;
    }
    removeForced(item);
  }

  @Override
  public void removeForced(@Nonnull final T item) {
    if (item.equals(selection)) {
      selection = null;
    }
  }

  @Override
  public void add(@Nonnull final T item) {
    selection = item;
  }

  @Override
  public void enableRequiresSelection(final boolean enable) {
    requiresSelection = enable;
  }

  @Override
  public boolean requiresAutoSelection() {
    return requiresSelection && selection == null;
  }
}
