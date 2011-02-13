package de.lessvoid.nifty.controls.nullobjects;

import java.util.List;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.tools.SizeValue;

public class ListBoxNull implements ListBox {

  @Override
  public String getId() {
    return "ListBoxNull";
  }

  @Override
  public void setId(final String id) {
  }

  @Override
  public SizeValue getWidth() {
    return null;
  }

  @Override
  public void setWidth(final SizeValue width) {
  }

  @Override
  public SizeValue getHeight() {
    return null;
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
  public void setListBoxViewConverter(final ListBoxViewConverter viewConverter) {
  }

  @Override
  public void addItem(final Object newItem) {
  }

  @Override
  public void insertItem(final Object item, final int index) {
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
  public void selectItem(final Object item) {
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
  public void deselectItem(final Object item) {
  }

  @Override
  public List getSelection() {
    return null;
  }

  @Override
  public void removeItemByIndex(final int itemIndex) {
  }

  @Override
  public void removeItem(final Object item) {
  }

  @Override
  public List getItems() {
    return null;
  }

  @Override
  public void showItem(final Object item) {
  }

  @Override
  public void showItemByIndex(final int itemIndex) {
  }

  @Override
  public void setFocusItem(final Object item) {
  }

  @Override
  public void setFocusItemByIndex(final int itemIndex) {
  }

  @Override
  public Object getFocusItem() {
    return null;
  }

  @Override
  public int getFocusItemIndex() {
    return 0;
  }

  @Override
  public void addAllItems(final List itemsToAdd) {
  }

  @Override
  public void removeAllItems(final List itemsToRemove) {
  }
}
