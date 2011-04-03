package de.lessvoid.nifty.controls;

import java.util.List;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the selection of the ListBox changes.
 * @author void
 */
public class ListBoxSelectionChangedEvent<T> implements NiftyEvent<T> {
  private List<T> selection;
  private List<Integer> selectionIndices;

  public ListBoxSelectionChangedEvent(final List<T> selection, final List<Integer> selectionIndices) {
    this.selection = selection;
    this.selectionIndices = selectionIndices;
  }

  public List<T> getSelection() {
    return selection;
  }

  public List<Integer> getSelectionIndices() {
    return selectionIndices;
  }
}
