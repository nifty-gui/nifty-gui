package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the selection of the DropDown changes.
 * @author void
 */
public class DropDownSelectionChangedEvent<T> implements NiftyEvent {
  private DropDown<T> dropDown;
  private T selection;
  private int selectionItemIndex;

  public DropDownSelectionChangedEvent(final DropDown<T> dropDown, final T selection, final int selectionItemIndex) {
    this.dropDown = dropDown;
    this.selection = selection;
    this.selectionItemIndex = selectionItemIndex;
  }

  public DropDown<T> getDropDown() {
    return dropDown;
  }

  public T getSelection() {
    return selection;
  }

  public int getSelectionItemIndex() {
    return selectionItemIndex;
  }
}
