package de.lessvoid.nifty.controls.listbox;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * A disabled selection mode for a Nifty ListBox. You can't select anything.
 *
 * @param <T>
 * @author void
 */
class ListBoxSelectionModeDisabled<T> implements ListBoxSelectionMode<T> {
  @Override
  public void clear() {
  }

  @Nonnull
  @Override
  public List<T> getSelection() {
    return Collections.emptyList();
  }

  @Override
  public void remove(@Nonnull final T item) {
  }

  @Override
  public void removeForced(@Nonnull final T item) {
  }

  @Override
  public void add(@Nonnull final T item) {
  }

  @Override
  public void enableRequiresSelection(final boolean enable) {
  }

  @Override
  public boolean requiresAutoSelection() {
    return false;
  }
}
