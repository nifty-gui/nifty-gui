package de.lessvoid.nifty.controls;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.nifty.controls.listbox.ListBoxItemProcessor;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;


/**
 * The ListBox interface is the Nifty control API view of a Nifty ListBox control.
 * @author void
 * @param <T>
 */
public interface ListBox<T> extends NiftyControl {

  /**
   * Change the {@link SelectionMode} to a new one.
   * @param listBoxSelectionMode the new {@link SelectionMode} to use
   * @param forceSelection if set to true will not allow de selecting the last item in the selection and
   *        it will automatically select the first item added. when set to false it's possible to have no
   *        selection at all.
   */
  void changeSelectionMode(SelectionMode listBoxSelectionMode, boolean forceSelection);

  /**
   * Change the ListBoxViewConverter for this ListBox.
   * @param viewConverter ListBoxViewConverter
   */
  void setListBoxViewConverter(ListBoxViewConverter<T> viewConverter);

  /**
   * Add a item to the ListBox.
   * @param newItem the item to add
   */
  void addItem(T newItem);

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
   * Select the next item. This will only work in SingleSelection mode and when there currently
   * is an element selected.
   */
  void selectNext();

  /**
   * Select the previous item. This will only work in SingleSelection mode and when there currently
   * is an element selected.
   */
  void selectPrevious();

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
   * Get the current selection as a list of indices.
   * @return list of indices for the current selection
   */
  List<Integer> getSelectedIndices();

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

  /**
   * Sort all items using natural ordering.
   */
  void sortAllItems();

  /**
   * Returns the number of items this ListBox can display without being scrolled.
   * @return number of display items
   */
  int getDisplayItemCount();

  /**
   * Sort all items using the given comperator.
   * @param comperator
   */
  void sortAllItems(Comparator<T> comperator);

  /**
   * Refresh the Listbox display. You can use that when you've made changes to the
   * underlying model classes. This just displays all currently visible elements.
   */
  void refresh();

  void addItemProcessor(ListBoxItemProcessor processor);

  /**
   * The ListBoxSelectionMode determines how the ListBox handles selections.
   * @author void
   */
  public enum SelectionMode {
    /**
     * Allows only a single item to be selected.
     * This is the default selection mode.
     */
    Single,

    /**
     * Allows multiple items to be selected.
     */
    Multiple,

    /**
     * Does not allow any selection at all.
     */
    Disabled
  }

  /**
   * You'll need to implement this interface to change the way your model class T needs
   * to be displayed in the ListBox. If you omit it then Nifty will use its default
   * implementation which simply calls T.toString();
   * @author void
   * @param <T>
   */
  public interface ListBoxViewConverter<T> {

    /**
     * Display the given item in the given element.
     *
     * @param listBoxItem the element to display the item in
     * @param item the item to display
     */
    void display(Element listBoxItem, T item);

    /**
     * Return the width in pixel of the given item rendered for the given element.
     *
     * @param element the element to render
     * @param item the item to render
     * @return the width of the element after the item has been applied to it
     */
    int getWidth(Element element, T item);
  }

  /**
   * A simple implementation of ListBoxViewConverter that will just use item.toString().
   * This is the default SimpleListBoxViewConverter used when you don't set a different implementation.
   *
   * @author void
   * @param <T>
   */
  public class ListBoxViewConverterSimple<T> implements ListBoxViewConverter<T> {
    private Logger log = Logger.getLogger(ListBoxViewConverterSimple.class.getName());

    @Override
    public void display(final Element element, final T item) {
      TextRenderer renderer = element.getRenderer(TextRenderer.class);
      if (renderer == null) {
        log.warning(
              "you're using the ListBoxViewConverterSimple but there is no TextRenderer on the listBoxElement."
            + "You've probably changed the item template but did not provided your own "
            + "ListBoxViewConverter to the ListBox.");
        return;
      }
      if (item != null) {
        renderer.setText(item.toString());
      } else {
        renderer.setText("");
      }
    }

    @Override
    public int getWidth(final Element element, final T item) {
      TextRenderer renderer = element.getRenderer(TextRenderer.class);
      if (renderer == null) {
        log.warning(
              "you're using the ListBoxViewConverterSimple but there is no TextRenderer on the listBoxElement."
            + "You've probably changed the item template but did not provided your own "
            + "ListBoxViewConverter to the ListBox.");
        return 0;
      }
     return element.getRenderer(TextRenderer.class).getFont().getWidth(item.toString());
    }
  }
}
