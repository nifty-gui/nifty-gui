package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Nifty generates this event when the selection of the ListBox changes.
 *
 * @author void
 */
public class ListBoxSelectionChangedEvent<T> implements NiftyEvent {
  @Nonnull
  private final ListBox<T> listBox;
  @Nonnull
  private final List<T> selection;
  @Nonnull
  private final List<Integer> selectionIndices;

  public ListBoxSelectionChangedEvent(
      @Nonnull final ListBox<T> listBox,
      @Nonnull final List<T> selection,
      @Nonnull final List<Integer> selectionIndices) {
    this.listBox = listBox;
    this.selection = selection;
    this.selectionIndices = selectionIndices;
  }

  @Nonnull
  public ListBox<T> getListBox() {
    return listBox;
  }

  @Nonnull
  public List<T> getSelection() {
    return selection;
  }

  @Nonnull
  public List<Integer> getSelectionIndices() {
    return selectionIndices;
  }
}
