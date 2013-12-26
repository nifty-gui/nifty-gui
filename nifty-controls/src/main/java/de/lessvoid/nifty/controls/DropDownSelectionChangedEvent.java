package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when the selection of the DropDown changes.
 *
 * @author void
 */
public class DropDownSelectionChangedEvent<T> implements NiftyEvent {
  @Nonnull
  private final DropDown<T> dropDown;
  @Nonnull
  private final T selection;
  private final int selectionItemIndex;

  public DropDownSelectionChangedEvent(
      @Nonnull final DropDown<T> dropDown,
      @Nonnull final T selection,
      final int selectionItemIndex) {
    this.dropDown = dropDown;
    this.selection = selection;
    this.selectionItemIndex = selectionItemIndex;
  }

  @Nonnull
  public DropDown<T> getDropDown() {
    return dropDown;
  }

  @Nonnull
  public T getSelection() {
    return selection;
  }

  public int getSelectionItemIndex() {
    return selectionItemIndex;
  }
}
