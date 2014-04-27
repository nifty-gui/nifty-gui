package de.lessvoid.nifty.controls.listbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

class ListBoxImpl<T> {
  @Nonnull
  private static final Logger log = Logger.getLogger(ListBoxImpl.class.getName());
  @Nonnull
  private final ListBox<T> listBox;
  @Nonnull
  private final List<T> items;
  @Nonnull
  private final List<ItemWidth<T>> widthList;
  @Nonnull
  private ListBoxSelectionMode<T> selection;
  @Nullable
  private ListBoxView<T> view;
  private int viewOffset = 0;
  private int viewDisplayItemCount = 0;
  private int focusItemIndex = -1;
  @Nonnull
  private final List<T> visibleItemsForDisplay;
  @Nonnull
  private final List<Integer> selectedItemsForDisplay;
  @Nonnull
  private final ListBoxFocusItem listBoxFocusItem;
  private int lastMaxWidth = 0;

  public ListBoxImpl(@Nonnull final ListBox<T> listBox) {
    this.listBox = listBox;
    items = new ArrayList<T>();
    widthList = new ArrayList<ItemWidth<T>>();
    selection = new ListBoxSelectionModeSingle<T>();
    visibleItemsForDisplay = new ArrayList<T>();
    selectedItemsForDisplay = new ArrayList<Integer>();
    listBoxFocusItem = new ListBoxFocusItem();
  }

  public int bindToView(@Nonnull final ListBoxView<T> newListBoxView, final int viewDisplayItemCount) {
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
    if (view != null) {
      view.display(updateCaptions(), getFocusItemForDisplay(), getSelectionElementsForDisplay());
    } else {
      log.warning("Updating the view is not possible as long as the view is not bound to this implementation.");
    }
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

  @Nullable
  public T getItemByVisualIndex(final int selectionIndex) {
    if (invalidVisualIndex(selectionIndex)) {
      return null;
    }
    if ((viewOffset + selectionIndex) >= items.size()) {
      return null;
    }
    return items.get(viewOffset + selectionIndex);
  }

  public void changeSelectionMode(
      @Nonnull final SelectionMode listBoxSelectionMode,
      final boolean forceSelection) {
    changeSelectionMode(listBoxSelectionMode, forceSelection, true);
  }

  void changeSelectionMode(
      @Nonnull final SelectionMode listBoxSelectionMode,
      final boolean forceSelection,
      final boolean raiseEvent) {
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
    if (raiseEvent) {
      selectionChangedEvent();
    }
  }

  public void addItem(@Nonnull final T newItem) {
    T visibleItem = getVisibleItem();

    widthList.add(new ItemWidth<T>(newItem, view == null ? 0 : view.getWidth(newItem)));
    items.add(newItem);
    widthUpdate();
    focusItemIndexUpdate();
    updateViewTotalCount();

    if (visibleItem != null) {
      restoreVisibleItem(visibleItem);
    }
    ensureAutoSelection(newItem);
  }

  public int itemCount() {
    return items.size();
  }

  public void clear() {
    items.clear();
    selection.clear();

    widthList.clear();
    lastMaxWidth = 0;
    if (view != null) {
      view.updateTotalWidth(lastMaxWidth);
    }

    focusItemIndexUpdate();
    updateViewTotalCount();
    selectionChangedEvent();
  }

  public void selectItemByIndex(final int selectionIndex) {
    if (invalidIndex(selectionIndex)) {
      return;
    }
    selection.add(items.get(selectionIndex));
    updateView();
    selectionChangedEvent();
    setFocusItemByIndex(selectionIndex);
  }

  public void selectItem(@Nonnull final T item) {
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
    return Collections.unmodifiableList(selection.getSelection());
  }

  @Nonnull
  public List<Integer> getSelectedIndices() {
    List<T> sel = selection.getSelection();
    if (sel.isEmpty()) {
      return Collections.emptyList();
    }

    List<Integer> result = new ArrayList<Integer>();
    for (T selItem : sel) {
      result.add(items.indexOf(selItem));
    }
    return result;
  }

  public void removeItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    int oldCount = itemCount();
    T visibleItem = getVisibleItem();

    T item = items.get(itemIndex);
    selection.removeForced(item);
    items.remove(itemIndex);
    widthList.remove(findItemIndexInWidthList(item));
    widthUpdate();

    listBoxFocusItem.prepare();
    listBoxFocusItem.registerIndex(itemIndex);

    updateAfterRemove(oldCount);
    if (visibleItem != null) {
      restoreVisibleItem(visibleItem);
    }
  }

  public void removeItem(final T item) {
    removeItemByIndex(items.indexOf(item));
  }

  public void removeAllItems(@Nonnull final Collection<T> itemsToRemove) {
    int oldCount = itemCount();
    T visibleItem = getVisibleItem();

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

    updateAfterRemove(oldCount);
    if (visibleItem != null) {
      restoreVisibleItem(visibleItem);
    }
  }

  public void deselectItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex)) {
      return;
    }
    selection.remove(items.get(itemIndex));
    updateView();
    selectionChangedEvent();
  }

  public void deselectItem(@Nonnull final T item) {
    deselectItemByIndex(items.indexOf(item));
  }

  @Nonnull
  public List<T> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void insertItem(@Nonnull final T item, final int index) {
    if (invalidIndexForInsert(index)) {
      return;
    }
    T visibleItem = getVisibleItem();
    widthList.add(new ItemWidth<T>(item, view == null ? 0 : view.getWidth(item)));
    items.add(index, item);
    widthUpdate();
    focusItemIndexUpdate();
    updateViewTotalCount();
    if (visibleItem != null) {
      restoreVisibleItem(visibleItem);
    }
    ensureAutoSelection(item);
  }

  public void showItem(@Nonnull final T item) {
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
    updateViewScroll();
    updateView();
  }

  public void setFocusItem(@Nullable final T item) {
    if (item == null) {
      setFocusItemByIndex(-1);
    } else {
      setFocusItemByIndex(items.indexOf(item));
    }
  }

  public void setFocusItemByIndex(final int itemIndex) {
    if (invalidIndex(itemIndex) || itemIndex == -1) {
      return;
    }
    focusItemIndex = itemIndex;

    if (focusItemIndex >= viewOffset + viewDisplayItemCount) {
      viewOffset = focusItemIndex - viewDisplayItemCount + 1;
      updateViewScroll();
      updateView();
    } else if (focusItemIndex < viewOffset) {
      showItemByIndex(focusItemIndex);
    } else {
      updateView();
    }
  }

  @Nullable
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

  public void addAllItems(@Nonnull final Collection<T> itemsToAdd) {
    if (itemsToAdd.isEmpty()) {
      return;
    }
    for (T item : itemsToAdd) {
      widthList.add(new ItemWidth<T>(item, view == null ? 0 : view.getWidth(item)));
    }
    T visibleItem = getVisibleItem();
    items.addAll(itemsToAdd);
    widthUpdate();
    focusItemIndexUpdate();
    updateViewTotalCount();
    if (visibleItem != null) {
      restoreVisibleItem(visibleItem);
    }
    if (!itemsToAdd.isEmpty()) {
      ensureAutoSelection(itemsToAdd.iterator().next());
    }
  }

  public void sortItems(@Nullable final Comparator<T> comparator) {
    Collections.sort(items, comparator);
  }

  void updateViewTotalCount() {
    if (view == null) {
      log.warning("Can't update total count of view while there is not view bound to the list box implementation.");
    } else {
      view.updateTotalCount(items.size());
      updateView();
    }
  }

  void updateViewScroll() {
    if (view == null) {
      log.warning("Can't perform view scrolling as long there is no view bound to the list box implementation.");
    } else {
      view.scrollTo(viewOffset);
    }
  }

  @Nonnull
  private List<Integer> getSelectionElementsForDisplay() {
    selectedItemsForDisplay.clear();
    List<T> selectionList = selection.getSelection();
    if (selectionList.isEmpty()) {
      return selectedItemsForDisplay;
    }
    for (T selectedItem : selectionList) {
      for (int i = 0; i < viewDisplayItemCount; i++) {
        int selectedItemIndex = items.indexOf(selectedItem);
        if (selectedItemIndex == viewOffset + i) {
          selectedItemsForDisplay.add(i);
        }
      }
    }
    return selectedItemsForDisplay;
  }

  @Nonnull
  private List<T> updateCaptions() {
    visibleItemsForDisplay.clear();
    for (int i = 0; i < viewDisplayItemCount; i++) {
      if (viewOffset + i < items.size()) {
        T item = items.get(viewOffset + i);
        visibleItemsForDisplay.add(item);
      } else {
        break;
      }
    }
    return visibleItemsForDisplay;
  }

  private int getFocusItemForDisplay() {
    for (int i = 0; i < viewDisplayItemCount; i++) {
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

  private void selectionChangedEvent() {
    if (view != null) {
      view.publish(new ListBoxSelectionChangedEvent<T>(listBox, getSelection(), getSelectedIndices()));
    }
  }

  private void updateAfterRemove(final int oldItemCount) {
    focusItemIndex = listBoxFocusItem.resolve(focusItemIndex, oldItemCount);
    focusItemIndexUpdate();

    if (selection.requiresAutoSelection() && itemCount() > 0 && focusItemIndex > -1) {
      selection.add(items.get(focusItemIndex));
    }

    if (view != null) {
      view.updateTotalCount(items.size());
    }

    if (viewOffset + viewDisplayItemCount > itemCount()) {
      if (itemCount() > 0) {
        showItemByIndex(itemCount() - 1);
        selectionChangedEvent();
        return;
      }
    }

    updateView();
    selectionChangedEvent();
  }

  private void widthUpdate() {
    if (widthList.isEmpty()) {
      if (lastMaxWidth != 0) {
        lastMaxWidth = 0;
        if (view != null) {
          view.updateTotalWidth(0);
        }
      }
      return;
    }
    Collections.sort(widthList);

    if (widthList.get(widthList.size() - 1).getWidth() != lastMaxWidth) {
      lastMaxWidth = widthList.get(widthList.size() - 1).getWidth();
      if (view != null) {
        view.updateTotalWidth(lastMaxWidth);
      }
    }
  }

  private int findItemIndexInWidthList(final T item) {
    for (int i = 0; i < widthList.size(); i++) {
      ItemWidth itemWidth = widthList.get(i);
      if (itemWidth.getItem().equals(item)) {
        return i;
      }
    }
    return -1;
  }

  private void ensureAutoSelection(@Nonnull final T newItem) {
    if (selection.requiresAutoSelection()) {
      selectItem(newItem);
    }
  }

  @Nonnull
  private ListBoxSelectionMode<T> createSelectionMode(@Nonnull final SelectionMode selectionMode) {
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

  @Nullable
  private T getVisibleItem() {
    return getItemByVisualIndex(0);
  }

  private void restoreVisibleItem(@Nonnull final T visibleItem) {
    showItem(visibleItem);
  }

  private static class ItemWidth<T> implements Comparable<ItemWidth<T>> {
    @Nonnull
    private final T item;
    private final int width;

    public ItemWidth(@Nonnull final T item, final int width) {
      this.item = item;
      this.width = width;
    }

    @Override
    public int compareTo(@Nonnull final ItemWidth a) {
      return Integer.valueOf(width).compareTo(a.width);
    }

    @Nonnull
    public T getItem() {
      return item;
    }

    public int getWidth() {
      return width;
    }
  }
}
