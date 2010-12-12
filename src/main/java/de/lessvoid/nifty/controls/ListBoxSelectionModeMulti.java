package de.lessvoid.nifty.controls;

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
    if (isPartOfSelection(item)) {
      removeFromSelection(item);
    }
  }

  @Override
  public void add(final T item) {
    selection.add(item);
  }

  private boolean isPartOfSelection(final T item) {
    return selection.contains(item);
  }

  private void removeFromSelection(final T item) {
    selection.remove(item);
  }
}
