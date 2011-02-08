package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the selection of the DropDown changes.
 * @author void
 */
public class DropDownSelectionChangedEvent<T> implements NiftyEvent<T> {
  private T selection;

  public DropDownSelectionChangedEvent(final T selection) {
    this.selection = selection;
  }

  public T getSelection() {
    return selection;
  }
}
