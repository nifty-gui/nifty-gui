package de.lessvoid.nifty.controls.listbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.shared.EmptyNiftyControlImpl;

public class ListBoxImpl<T> extends EmptyNiftyControlImpl {
  private List<T> items = new ArrayList<T>();
  private List<ItemWidth> widthList = new ArrayList<ItemWidth>();
  private ListBoxSelectionMode<T> selection = new ListBoxSelectionModeSingle<T>();
  private ListBoxView<T> view = new ListBoxViewNull<T>();
  private int viewOffset = 0;
  private int viewDisplayItemCount = 0;
  private int focusItemIndex = -1;
  private List<T> visibleItemsForDisplay = new ArrayList<T>();
  private List<Integer> selectedItemsForDisplay = new ArrayList<Integer>();
  private ListBoxFocusItem listBoxFocusItem = new ListBoxFocusItem();
  private int lastMaxWidth = 0;

  public int bindToView(final ListBoxView<T> newListBoxView, final int viewDisplayItemCount) {
    this.view = newListBoxView;
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
    view.display(updateCaptions(), getFocusItemForDisplay(), getSelectionElementsForDisplay());
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

  public void changeSelectionMode(final SelectionMode listBoxSelectionMode, final boolean forceSelection) {
    List<T> oldSelection = getSelection();

    selection = createSelectionMode(listBoxSelectionMode);
    selection.enableRequiresSelection(forceSelection);

    ListIterator<T> it = oldSelection.listIterator(oldSelection.size());
    while (it.hasPrevious()) {
      selection.add(it.previous());
    }

    if (selection.requiresAutoSelection() && itemCount() > 0) {
      selection.add(items.get(0));
    }

    updateView();
    selectionChangedEvent(oldSelection);
  }

  public void addItem(final T newItem) {
    widthList.add(new ItemWidth(newItem));
    items.add(newItem);
    widthUpdate();
    focusItemIndexUpdate();
    updateViewTotalCount();
    ensureAutoSelection(newItem);
  }

  public int itemCount() {
    return items.size();
  }

  public void clear() {
    List<T> oldSelection = getSelection();
    items.clear();
    selection.clear();

    widthList.clear();
    lastMaxWidth = 0;
    view.updateTotalWidth(lastMaxWidth);

    focusItemIndexUpdate();
    updateViewTotalCount();
    selectionChangedEvent(oldSelection);
  }

  public void selectItemByIndex(final int selectionIndex) {
    if (invalidIndex(selectionIndex)) {
      return;
    }
    List<T> oldSelection = getSelection();
    selection.add(items.get(selectionIndex));
    updateView();
    selectionChangedEvent(oldSelection);
  }

  public void selectItem(final T item) {
    selectItemByIndex(items.indexOf(item));
  }

  public void selectNext() {
    if (!(selection instanceof ListBoxSelectionModeSingle)) {
      return;
    }
    if (selection.getSelection().isEmpty()) {
      return;
    }
    int selectionIndex = items.indexOf(selection.getSelection().get(0));
    if (invalidIndex(selectionIndex)) {
      return;
    }
    selectionIndex++;
    if (invalidIndex(selectionIndex)) {
      return;
    }
    selectItemByIndex(selectionIndex);
    setFocusItemByIndex(selectionIndex);
  }

  public void selectPrevious() {
    if (!(selection instanceof ListBoxSelectionModeSingle)) {
      return;
    }
    if (selection.getSelection().isEmpty()) {
      return;
    }
    int selectionIndex = items.indexOf(selection.getSelection().get(0));
    if (invalidIndex(selectionIndex)) {
      return;
    }
    selectionIndex--;
    if (invalidIndex(selectionIndex)) {
      return;
    }
    selectItemByIndex(selectionIndex);
    setFocusItemByIndex(selectionIndex);
  }

  public List<T> getSelection() {
    return new ArrayList<T>(selection.getSelection());
  }

  public void removeItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    List<T> oldSelection = getSelection();
    int oldCount = itemCount();

    T item = items.get(itemIndex);
    selection.removeForced(item);
    items.remove(itemIndex);
    widthList.remove(findItemIndexInWidthList(item));
    widthUpdate();

    listBoxFocusItem.prepare();
    listBoxFocusItem.registerIndex(itemIndex);

    updateAfterRemove(oldSelection, oldCount);
  }

  public void removeItem(final T item) {
    removeItemByIndex(items.indexOf(item));
  }

  public void removeAllItems(final List<T> itemsToRemove) {
    List<T> oldSelection = getSelection();
    int oldCount = itemCount();

    listBoxFocusItem.prepare();
    for (T item : itemsToRemove) {
      listBoxFocusItem.registerIndex(items.indexOf(item));
      widthList.remove(findItemIndexInWidthList(item));
    }

    widthUpdate();

    if (!items.removeAll(itemsToRemove)) {
      return;
    }

    for (T item : selection.getSelection()) {
      selection.removeForced(item);
    }

    updateAfterRemove(oldSelection, oldCount);
  }

  public void deselectItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    List<T> oldSelection = getSelection();
    selection.remove(items.get(itemIndex));
    updateView();
    selectionChangedEvent(oldSelection);
  }

  public void deselectItem(final T item) {
    deselectItemByIndex(items.indexOf(item));
  }

  public List<T> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void insertItem(final T item, final int index) {
    if (invalidIndexForInsert(index)) {
      return;
    }
    widthList.add(new ItemWidth(item));
    items.add(index, item);
    widthUpdate();
    focusItemIndexUpdate();
    updateViewTotalCount();
    ensureAutoSelection(item);
  }

  public void showItem(final T item) {
    showItemByIndex(items.indexOf(item));
  }

  public void showItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    viewOffset = itemIndex;
    if (itemCount() <= viewDisplayItemCount) {
      viewOffset = 0;
    } else if (itemIndex > items.size() - viewDisplayItemCount) {
      viewOffset = items.size() - viewDisplayItemCount;
    }
    view.scrollTo(viewOffset);
    updateView();
  }

  public void setFocusItem(final T item) {
    setFocusItemByIndex(items.indexOf(item));
  }

  public void setFocusItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    focusItemIndex = itemIndex;

    if (focusItemIndex >= viewOffset + viewDisplayItemCount) {
      viewOffset = focusItemIndex - viewDisplayItemCount + 1;
      view.scrollTo(viewOffset);
      updateView();
    } else if (focusItemIndex < viewOffset) {
      showItemByIndex(focusItemIndex);
    } else {
      updateView();
    }
  }

  public T getFocusItem() {
    if (focusItemIndex == -1) {
      return null;
    }
    return items.get(focusItemIndex);
  }

  public int getFocusItemIndex() {
    return focusItemIndex;
  }

  public void setListBoxViewConverter(final ListBoxViewConverter<T> viewConverter) {
    // handled in ListBoxControl directly
  }

  public void addAllItems(final List<T> itemsToAdd) {
    if (itemsToAdd.isEmpty()) {
      return;
    }
    for (T item : itemsToAdd) {
      widthList.add(new ItemWidth(item));
    }
    items.addAll(itemsToAdd);
    widthUpdate();
    focusItemIndexUpdate();
    updateViewTotalCount();
    ensureAutoSelection(itemsToAdd.get(0));
  }

  public void sortItems(final Comparator<T> comperator) {
    Collections.sort(items, comperator);
  }

  private void updateViewTotalCount() {
    view.updateTotalCount(items.size());
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
    if (selectionIndex >= itemCount()) {
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
    if (items.size() == 0) {
      focusItemIndex = -1;
      return;
    }
    if (items.size() == 1) {
      focusItemIndex = 0;
      return;
    }
    if (focusItemIndex == -1 && itemCount() > 0) {
      focusItemIndex = 0;
    }
  }

  private void selectionChangedEvent(final List<T> oldSelection) {
    view.publish(new ListBoxSelectionChangedEvent<T>(Collections.unmodifiableList(selection.getSelection())));
  }

  private void updateAfterRemove(final List<T> oldSelection, final int oldItemCount) {
    focusItemIndex = listBoxFocusItem.resolve(focusItemIndex, oldItemCount);
    focusItemIndexUpdate();

    if (selection.requiresAutoSelection() && itemCount() > 0 && focusItemIndex > -1) {
      selection.add(items.get(focusItemIndex));
    }

    view.updateTotalCount(items.size());

    if (viewOffset + viewDisplayItemCount > itemCount()) {
      if (itemCount() > 0) {
        showItemByIndex(itemCount() - 1);
        selectionChangedEvent(oldSelection);
        return;
      }
    }

    updateView();
    selectionChangedEvent(oldSelection);
  }

  private void widthUpdate() {
    if (widthList.isEmpty()) {
      if (lastMaxWidth != 0) {
        lastMaxWidth = 0;
        view.updateTotalWidth(0);
      }
      return;
    }
    Collections.sort(widthList);

    if (widthList.get(widthList.size() - 1).getWidth() != lastMaxWidth) {
      lastMaxWidth = widthList.get(widthList.size() - 1).getWidth();
      view.updateTotalWidth(lastMaxWidth);
    }
  }

  private int findItemIndexInWidthList(final T item) {
    for (int i=0; i<widthList.size(); i++) {
      ItemWidth itemWidth = widthList.get(i);
      if (itemWidth.getItem().equals(item)) {
        return i;
      }
    }
    return -1;
  }

  private void ensureAutoSelection(final T newItem) {
    if (selection.requiresAutoSelection()) {
      selectItem(newItem);
    }
  }

  private ListBoxSelectionMode<T> createSelectionMode(final SelectionMode selectionMode) {
    switch (selectionMode) {
      case Single:
        return new ListBoxSelectionModeSingle<T>();

      case Multiple:
        return new ListBoxSelectionModeMulti<T>();

      case Disabled:
        return new ListBoxSelectionModeDisabled<T>();

      default:
        return new ListBoxSelectionModeSingle<T>();
    }
  }

  public class ItemWidth implements Comparable<ItemWidth> {
    private T item;
    private int width;

    public ItemWidth(final T item) {
      this.item = item;
      this.width = view.getWidth(item);
    }

    @Override
    public int compareTo(final ItemWidth a) {
      return Integer.valueOf(width).compareTo(a.width);
    }

    @Override
    public boolean equals(final Object obj) {
      return item.equals(obj);
    }

    @Override
    public int hashCode() {
      return item.hashCode();
    }

    public T getItem() {
      return item;
    }

    public int getWidth() {
      return width;
    }
  }
}
