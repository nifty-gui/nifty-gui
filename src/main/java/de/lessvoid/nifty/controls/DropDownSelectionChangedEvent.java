package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the selection of the DropDown changes.
 * @author void
 */
public class DropDownSelectionChangedEvent<T> implements NiftyEvent<T> {
  private T selection;
  private int selectionItemIndex;

  public DropDownSelectionChangedEvent(final T selection, final int selectionItemIndex) {
    this.selection = selection;
    this.selectionItemIndex = selectionItemIndex;
  }

  public T getSelection() {
    return selection;
  }

  public int getSelectionItemIndex() {
    return selectionItemIndex;
  }
}
