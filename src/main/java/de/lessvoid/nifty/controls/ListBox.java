package de.lessvoid.nifty.controls;

import java.util.List;


/**
 * The ListBox interface is the Nifty control API view of a Nifty ListBox control.
 * @author void
 * @param <T>
 */
public interface ListBox<T> extends NiftyControl {

  /**
   * Change the {@link ListBoxSelectionMode} to a new one.
   * @param listBoxSelectionMode the new {@link ListBoxSelectionMode} to use
   */
  void changeSelectionMode(ListBoxSelectionMode<T> listBoxSelectionMode);

  /**
   * Change the ListBoxViewConverter for this ListBox.
   * @param viewConverter ListBoxViewConverter
   */
  void setListBoxViewConverter(ListBoxViewConverter<T> viewConverter);

  /**
   * Add a item to the ListBox.
   * @param newItem the item to add
   */
  void addItem(final T newItem);

  /**
   * Insert the given item at the given index.
   * @param item item
   * @param index the index to insert the item.
   */
  void insertItem(T item, int index);

  /**
   * Retrieve the number of items in the ListBox.
   * @return number of items.
   */
  int itemCount();

  /**
   * Clear all items from this ListBox.
   */
  void clear();

  /**
   * Select the item with the given index in the ListBox. This might change
   * the currently selected item if the {@link ListBoxSingleSelectionMode} is used or
   * it will add to the selection if {@link ListBoxMultiSelectionMode} is used.
   * @param selectionIndex the item index to select in the ComboBox
   */
  void selectItemByIndex(final int selectionIndex);

  /**
   * Select the item in the ListBox.
   * @param item the item to select
   */
  void selectItem(final T item);

  /**
   * Deselect the item with the given itemIndex.
   * @param itemIndex item index to deselect
   */
  void deselectItemByIndex(int itemIndex);

  /**
   * Deselect the given item.
   * @param item item to deselect.
   */
  void deselectItem(T item);

  /**
   * Get the current selection.
   * @return list of the selected items in this ListBox.
   */
  List<T> getSelection();

  /**
   * Remove an item from the ListBox by index.
   * @param itemIndex remove the item with the given index from the ListBox
   */
  void removeItemByIndex(int itemIndex);

  /**
   * Remove the given item from the ListBox.
   * @param item the item to remove from the ListBox
   */
  void removeItem(T item);

  /**
   * Get all items of this ListBox.
   * @return list of all items
   */
  List<T> getItems();

  /**
   * Make sure the given item is visible. This can also be used to make sure
   * you can see the element after a new item has been added to the ListBox.
   * @param item the item
   */
  void showItem(T item);

  /**
   * Make sure the given item is visible.
   * @param itemIndex the item index to make visible
   */
  void showItemByIndex(int itemIndex);

  /**
   * Change the current focus item to the item given. The focus item is the item
   * you can change with the cursor keys. It just marks the item it does not change
   * the selection.
   * @param item the item to set the focus to
   */
  void setFocusItem(T item);

  /**
   * Change the current focus item to the given index.
   * @param itemIndex the new focus item
   */
  void setFocusItemByIndex(int itemIndex);

  /**
   * Get the current item that has the focus.
   * @return the item that has the focus
   */
  T getFocusItem();

  /**
   * Get the index of the current focus item.
   * @return the index of the current focus item.
   */
  int getFocusItemIndex();

  /**
   * Add all items to the ListBox.
   * @param itemsToAdd all items to add
   */
  void addAllItems(List<T> itemsToAdd);

  /**
   * Remove all items given in the List from this ListBox.
   * @param itemsToRemove list of items to remove
   */
  void removeAllItems(List<T> itemsToRemove);
}