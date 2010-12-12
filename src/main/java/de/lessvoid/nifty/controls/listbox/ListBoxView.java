package de.lessvoid.nifty.controls.listbox;

import java.util.List;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

/**
 * The representation of a ListBoxView from the world of a ListBox.
 * @author void
 *
 * @param <T> The Item this class is a view for.
 */
public interface ListBoxView<T> {

  /**
   * Display the given descriptions.
   * @param captions
   */
  void display(List<T> captions, int focusElementIndex, List<Integer> selectionElements);

  /**
   * Updates the view with the total count of elements currently in the ListBox.
   * This can be used to update the scrollbar.
   * @param newCount the new count to display
   */
  void updateTotalCount(int newCount);

  /**
   * Scroll the view to the given position.
   * @param newPosition the new index to scroll to
   */
  void scrollTo(int newPosition);

  /**
   * Publish this event.
   * @param event the event to publish
   */
  void publish(ListBoxSelectionChangedEvent<T> event);
}
