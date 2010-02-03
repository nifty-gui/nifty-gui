package de.lessvoid.nifty.controls.dropdown.controller;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dropdown.CreateDropDownControlItem;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * The actual Data the DropDownControl handles.
 * @author void
 */
public class DropDownModel {
  private List < String > items = new ArrayList < String >();
  private int selectedItemIdx = -1;

  public void addItem(final String description) {
    items.add(description);
  }

  public void initialize(
      final Nifty nifty,
      final Screen screen,
      final Element parent,
      final String style) {
    int count = 0;
    for (String item : items) {
      String id = parent.getId() + "_" + count++;
      CreateDropDownControlItem dropDownItem = new CreateDropDownControlItem(id, item);
      dropDownItem.create(nifty, screen, parent, style);
    }
  }

  public void setSelectedItemIdx(final int idx) {
    selectedItemIdx  = idx;
  }

  public void setSelectedItem(final String text) {
    selectedItemIdx = items.indexOf(text);
  }

  public String getSelectedItem() {
    return items.get(selectedItemIdx);
  }

  public int getSelectedItemIdx() {
    return selectedItemIdx;
  }

  public void clear() {
    items.clear();
  }

  public int gotoNextItem() {
    if (selectedItemIdx < items.size() - 1) {
      selectedItemIdx++;
    } else {
      selectedItemIdx = 0;
    }
    return selectedItemIdx;
  }

  public int gotoPrevItem() {
    if (selectedItemIdx > 0) {
      selectedItemIdx--;
    } else {
      selectedItemIdx = items.size() - 1;
    }
    return selectedItemIdx;
  }
}
