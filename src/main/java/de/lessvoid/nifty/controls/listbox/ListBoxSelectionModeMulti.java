package de.lessvoid.nifty.controls.listbox;

import java.util.ArrayList;
import java.util.List;

/**
 * A multiple selection mode for a Nifty ListBox. You can select multiple items.
 * Selecting a new one will add to any previous selected items.
 * @author void
 * @param <T>
 */
public class ListBoxSelectionModeMulti<T> implements ListBoxSelectionMode<T> {
  private List<T> selection = new ArrayList<T>();
  private boolean requiresSelection = false;

  @Override
  public void clear() {
    selection.clear();
  }

  @Override
  public List<T> getSelection() {
    return new ArrayList<T>(selection);
  }

  @Override
  public void remove(final T item) {
    if (requiresSelection && selection.size() < 2) {
      return;
    }
    if (isPartOfSelection(item)) {
      removeFromSelection(item);
    }
  }

  @Override
  public void removeForced(final T item) {
    if (isPartOfSelection(item)) {
      removeFromSelection(item);
    }
  }

  @Override
  public void add(final T item) {
    selection.add(item);
  }

  @Override
  public void enableRequiresSelection(final boolean enabled) {
    requiresSelection = enabled;
  }

  @Override
  public boolean requiresAutoSelection() {
    return requiresSelection && selection.isEmpty();
  }

  private boolean isPartOfSelection(final T item) {
    return selection.contains(item);
  }

  private void removeFromSelection(final T item) {
    selection.remove(item);
  }
}
