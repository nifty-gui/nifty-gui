package de.lessvoid.nifty.controls;

import java.util.List;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the selection of the ListBox changes.
 * @author void
 */
public class ListBoxSelectionChangedEvent<T> implements NiftyEvent<T> {
  private List<T> selection;

  public ListBoxSelectionChangedEvent(final List<T> selection) {
    this.selection = selection;
  }

  public List<T> getSelection() {
    return selection;
  }
}
