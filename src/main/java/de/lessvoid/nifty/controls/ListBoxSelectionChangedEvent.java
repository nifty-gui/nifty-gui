package de.lessvoid.nifty.controls;

import java.util.List;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the selection of the ListBox changes.
 * @author void
 */
public class ListBoxSelectionChangedEvent<T> implements NiftyEvent<T> {
  private ListBox<T> listBox;
  private List<T> selection;
  private List<Integer> selectionIndices;

  public ListBoxSelectionChangedEvent(final ListBox<T> listBox, final List<T> selection, final List<Integer> selectionIndices) {
    this.listBox = listBox;
    this.selection = selection;
    this.selectionIndices = selectionIndices;
  }

  public ListBox<T> getListBox() {
    return listBox;
  }

  public List<T> getSelection() {
    return selection;
  }

  public List<Integer> getSelectionIndices() {
    return selectionIndices;
  }
}
