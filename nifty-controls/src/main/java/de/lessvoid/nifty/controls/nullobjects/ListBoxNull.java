package de.lessvoid.nifty.controls.nullobjects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public class ListBoxNull<T> implements ListBox<T> {

  @Override
  public Element getElement() {
    return null;
  }

  @Override
  public String getId() {
    return "ListBoxNull";
  }

  @Override
  public void setId(final String id) {
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public void setWidth(final SizeValue width) {
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public void setHeight(final SizeValue height) {
  }

  @Override
  public String getStyle() {
    return null;
  }

  @Override
  public void setStyle(final String style) {
  }

  @Override
  public void enable() {
  }

  @Override
  public void disable() {
  }

  @Override
  public void setEnabled(final boolean enabled) {
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public void changeSelectionMode(final SelectionMode listBoxSelectionMode, final boolean forceSelection) {
  }

  @Override
  public void setListBoxViewConverter(final ListBoxViewConverter<T> viewConverter) {
  }

  @Override
  public void addItem(final T newItem) {
  }

  @Override
  public void insertItem(final T item, final int index) {
  }

  @Override
  public int itemCount() {
    return 0;
  }

  @Override
  public void clear() {
  }

  @Override
  public void selectItemByIndex(final int selectionIndex) {
  }

  @Override
  public void selectItem(final T item) {
  }

  @Override
  public void selectNext() {
  }

  @Override
  public void selectPrevious() {
  }

  @Override
  public void deselectItemByIndex(final int itemIndex) {
  }

  @Override
  public void deselectItem(final T item) {
  }

  @Override
  public List<T> getSelection() {
    return null;
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
  }

  @Override
  public void removeItem(final T item) {
  }

  @Override
  public List<T> getItems() {
    return null;
  }

  @Override
  public void showItem(final T item) {
  }

  @Override
  public void showItemByIndex(final int itemIndex) {
  }

  @Override
  public void setFocusItem(final T item) {
  }

  @Override
  public void setFocusItemByIndex(final int itemIndex) {
  }

  @Override
  public T getFocusItem() {
    return null;
  }

  @Override
  public int getFocusItemIndex() {
    return 0;
  }

  @Override
  public void addAllItems(final List<T> itemsToAdd) {
  }

  @Override
  public void removeAllItems(final List<T> itemsToRemove) {
  }

  @Override
  public void sortAllItems() {
  }

  @Override
  public void sortAllItems(final Comparator<T> comperator) {
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void setFocusable(final boolean focusable) {
  }

  @Override
  public List<Integer> getSelectedIndices() {
    return new ArrayList<Integer>();
  }

  @Override
  public int getDisplayItemCount() {
    return 0;
  }

  @Override
  public boolean hasFocus() {
    return false;
  }

  @Override
  public void layoutCallback() {
  }

  @Override
  public boolean isBound() {
    return false;
  }

  @Override
  public void refresh() {
  }
}
