package de.lessvoid.nifty.controls.listbox;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

class ListBoxFocusItem {
  @Nonnull
  private final List<Integer> indicesToRemove = new ArrayList<Integer>();

  public void prepare() {
    indicesToRemove.clear();
  }

  public void registerIndex(final int value) {
    indicesToRemove.add(value);
  }

  public int resolve(final int focusIndex, final int itemCount) {
    if (focusIndex == -1) {
      return -1;
    }
    if (itemCount == 0) {
      return -1;
    }
    if (indicesToRemove.isEmpty()) {
      return focusIndex;
    }
    int newFocusIndex = calcNewFocusIndex(focusIndex, indicesToRemove);
    if (newFocusIndex >= itemCount - indicesToRemove.size()) {
      newFocusIndex = itemCount - indicesToRemove.size() - 1;
    }
    return newFocusIndex;
  }

  private int calcNewFocusIndex(final int focusIndex, @Nonnull final List<Integer> indicesToRemove) {
    int newFocusIndex = focusIndex;
    for (int i = 0; i < indicesToRemove.size(); i++) {
      int removeIdx = indicesToRemove.get(i);
      if (removeIdx < newFocusIndex) {
        newFocusIndex--;
        decrementListEntries(indicesToRemove, i);
      }
    }
    return newFocusIndex;
  }

  private void decrementListEntries(@Nonnull final List<Integer> indicesToRemove, final int startIdx) {
    for (int i = startIdx; i < indicesToRemove.size(); i++) {
      indicesToRemove.set(i, indicesToRemove.get(i) - 1);
    }
  }
}
