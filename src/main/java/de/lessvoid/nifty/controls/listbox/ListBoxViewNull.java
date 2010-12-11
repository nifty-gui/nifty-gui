package de.lessvoid.nifty.controls.listbox;

import java.util.List;


/**
 * A Null implementation of ListBoxViewNull that does nothing.
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
}
