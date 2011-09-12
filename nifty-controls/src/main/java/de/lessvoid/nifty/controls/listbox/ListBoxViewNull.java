package de.lessvoid.nifty.controls.listbox;

import java.util.List;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;


/**
 * A Null implementation of ListBoxView that does nothing.
 * @author void
 */
public class ListBoxViewNull<T> implements ListBoxView<T> {

  @Override
  public void display(final List<T> captions, final int focusElement, final List<Integer> selectionElements) {
  }

  @Override
  public void updateTotalCount(final int newCount) {
  }

  @Override
  public void scrollTo(final int newPosition) {
  }

  @Override
  public void publish(final ListBoxSelectionChangedEvent<T> event) {
  }

  @Override
  public void updateTotalWidth(final int newWidth) {
  }

  @Override
  public int getWidth(final T item) {
    return 0;
  }
}
