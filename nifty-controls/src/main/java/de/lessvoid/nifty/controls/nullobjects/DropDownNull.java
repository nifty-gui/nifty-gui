package de.lessvoid.nifty.controls.nullobjects;

import java.util.List;

import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public class DropDownNull<T> implements DropDown<T> {

  @Override
  public Element getElement() {
    return null;
  }

  @Override
  public String getId() {
    return "DropDownNull";
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
  public void setViewConverter(final DropDownViewConverter<T> viewConverter) {
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
  public T getSelection() {
    return null;
  }

  @Override
  public int getSelectedIndex() {
    return -1;
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
  public void addAllItems(final List<T> itemsToAdd) {
  }

  @Override
  public void removeAllItems(final List<T> itemsToRemove) {
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void setFocusable(final boolean focusable) {
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
}
