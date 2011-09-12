package de.lessvoid.nifty.controls.listbox;

import java.util.ArrayList;
import java.util.List;

public class ListBoxFocusItem {
  private List<Integer> indizesToRemove = new ArrayList<Integer>();

  public void prepare() {
    indizesToRemove.clear();
  }

  public void registerIndex(final int value) {
    indizesToRemove.add(value);
  }

  public int resolve(final int focusIndex, final int itemCount) {
    if (focusIndex == -1) {
      return -1;
    }
    if (itemCount == 0) {
      return -1;
    }
    if (indizesToRemove.isEmpty()) {
      return focusIndex;
    }
    int newFocusIndex = calcNewFocusIndex(focusIndex, indizesToRemove);
    if (newFocusIndex >= itemCount - indizesToRemove.size()) {
      newFocusIndex = itemCount - indizesToRemove.size() - 1;
    }
    return newFocusIndex;
  }

  private int calcNewFocusIndex(final int focusIndex, final List<Integer> indizesToRemove) {
    int newFocusIndex = focusIndex;
    for (int i=0; i<indizesToRemove.size(); i++) {
      int removeIdx = indizesToRemove.get(i);
      if (removeIdx < newFocusIndex) {
        newFocusIndex--;
        decrementListEntries(indizesToRemove, i);
      }
    }
    return newFocusIndex;
  }

  private void decrementListEntries(final List<Integer> indizesToRemove, final int startIdx) {
    for (int i=startIdx; i<indizesToRemove.size(); i++) {
      indizesToRemove.set(i, indizesToRemove.get(i) - 1);
    }
  }
}
