package de.lessvoid.nifty.controls.listbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lessvoid.nifty.controls.ListBox;

public class ListBoxImpl<T> implements ListBox<T> {
  private List<T> items = new ArrayList<T>();
  private ListBoxSelectionMode<T> selection = new ListBoxSingleSelectionMode<T>();
  private ListBoxView<T> listBoxView = new ListBoxViewNull<T>();
  private int viewOffset = 0;
  private int viewDisplayItemCount = 0;
  private int focusItemIndex = -1;
  private List<T> visibleItemsForDisplay = new ArrayList<T>();
  private List<Integer> selectedItemsForDisplay = new ArrayList<Integer>();

  public int bindToView(final ListBoxView<T> newListBoxView, final int viewDisplayItemCount) {
    this.listBoxView = newListBoxView;
    this.viewDisplayItemCount = viewDisplayItemCount;
    return items.size();
  }

  public void updateView(final int newViewOffset) {
    if (newViewOffset > 0 && newViewOffset >= items.size()) {
      return;
    }
    viewOffset = newViewOffset;
    updateView();
  }

  public void updateView() {
    listBoxView.display(updateCaptions(), getFocusItemForDisplay(), getSelectionElementsForDisplay());
  }

  public void selectItemByVisualIndex(final int selectionIndex) {
    if (invalidVisualIndex(selectionIndex)) {
      return;
    }
    selectItemByIndex(viewOffset + selectionIndex);
  }

  public void deselectItemByVisualIndex(final int selectionIndex) {
    if (invalidVisualIndex(selectionIndex)) {
      return;
    }
    deselectItemByIndex(viewOffset + selectionIndex);
  }

  public T getItemByVisualIndex(final int selectionIndex) {
    if (invalidVisualIndex(selectionIndex)) {
      return null;
    }
    return items.get(viewOffset + selectionIndex);
  }

  @Override
  public void setSelectionMode(final ListBoxSelectionMode<T> listBoxSelectionMode) {
    selection = listBoxSelectionMode;
  }

  @Override
  public void addItem(final T newItem) {
    items.add(newItem);
    focusItemIndexUpdate();
    updateViewTotalCount();
  }

  @Override
  public int itemCount() {
    return items.size();
  }

  @Override
  public void clear() {
    items.clear();
    selection.clear();
    focusItemIndexUpdate();
    updateViewTotalCount();
  }

  @Override
  public void selectItemByIndex(final int selectionIndex) {
    if (invalidIndex(selectionIndex)) {
      return;
    }
    selection.add(items.get(selectionIndex));
    updateView();
  }

  @Override
  public void selectItem(final T item) {
    selectItemByIndex(items.indexOf(item));
  }

  @Override
  public List<T> getSelection() {
    return selection.getSelection();
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    selection.remove(items.get(itemIndex));
    items.remove(itemIndex);
    focusItemIndexUpdate();
    updateViewTotalCount();
  }

  @Override
  public void removeItem(final T item) {
    if (!items.remove(item)) {
      return;
    }
    selection.remove(item);
    focusItemIndexUpdate();
    updateViewTotalCount();
  }

  @Override
  public void deselectItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    selection.remove(items.get(itemIndex));
    updateView();
  }

  @Override
  public void deselectItem(final T item) {
    deselectItemByIndex(items.indexOf(item));
  }

  @Override
  public List<T> getItems() {
    return Collections.unmodifiableList(items);
  }

  @Override
  public void insertItem(final T item, final int index) {
    if (invalidIndexForInsert(index)) {
      return;
    }
    items.add(index, item);
    focusItemIndexUpdate();
    updateViewTotalCount();
  }

  @Override
  public void showItem(final T item) {
    showItemByIndex(items.indexOf(item));
  }

  @Override
  public void showItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    if (itemIndex > items.size() - viewDisplayItemCount) {
      listBoxView.scrollTo(items.size() - viewDisplayItemCount);
    } else {
      listBoxView.scrollTo(itemIndex);
    }
  }

  @Override
  public void setFocusItem(final T item) {
    setFocusItemByIndex(items.indexOf(item));
  }

  @Override
  public void setFocusItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    focusItemIndex = itemIndex;
    updateView();

    if (focusItemIndex >= viewOffset + viewDisplayItemCount) {
      listBoxView.scrollTo(focusItemIndex - viewDisplayItemCount + 1);
    } else if (focusItemIndex < viewOffset) {
      showItemByIndex(focusItemIndex);
    }
  }

  @Override
  public T getFocusItem() {
    if (focusItemIndex == -1) {
      return null;
    }
    return items.get(focusItemIndex);
  }

  @Override
  public int getFocusItemIndex() {
    return focusItemIndex;
  }

  private void updateViewTotalCount() {
    listBoxView.updateTotalCount(items.size());
    updateView();
  }

  private List<Integer> getSelectionElementsForDisplay() {
    selectedItemsForDisplay.clear();
    List<T> selectionList = selection.getSelection();
    if (selectionList.isEmpty()) {
      return selectedItemsForDisplay;
    }
    for (T selectedItem : selectionList) {
      for (int i=0; i<viewDisplayItemCount; i++) {
        int selectedItemIndex = items.indexOf(selectedItem);
        if (selectedItemIndex == viewOffset + i) {
          selectedItemsForDisplay.add(i);
        }
      }
    }
    return selectedItemsForDisplay;
  }

  private List<T> updateCaptions() {
    visibleItemsForDisplay.clear();
    for (int i=0; i<viewDisplayItemCount; i++) {
      T item = null;
      if (viewOffset + i < items.size()) {
        item = items.get(viewOffset + i);
      }
      visibleItemsForDisplay.add(item);
    }
    return visibleItemsForDisplay;
  }

  private int getFocusItemForDisplay() {
    for (int i=0; i<viewDisplayItemCount; i++) {
      if (viewOffset + i < items.size()) {
        if (focusItemIndex == viewOffset + i) {
          return i;
        }
      }
    }
    return -1;
  }

  private boolean invalidVisualIndex(final int selectionIndex) {
    if (selectionIndex < 0) {
      return true;
    }
    if (selectionIndex >= viewDisplayItemCount) {
      return true;
    }
    return false;
  }

  private boolean invalidIndex(final int itemIndex) {
    if (itemIndex < 0) {
      return true;
    }
    if (itemIndex >= items.size()) {
      return true;
    }
    return false;
  }

  private boolean invalidIndexForInsert(final int itemIndex) {
    if (itemIndex < 0) {
      return true;
    }
    if (itemIndex > items.size()) {
      return true;
    }
    return false;
  }

  private void focusItemIndexUpdate() {
    if (items.size() == 1) {
      focusItemIndex = 0;
    } else if (items.size() == 0) {
      focusItemIndex = -1;
    }
  }

  @Override
  public void setListBoxViewConverter(final ListBoxViewConverter<T> viewConverter) {
  }
}
