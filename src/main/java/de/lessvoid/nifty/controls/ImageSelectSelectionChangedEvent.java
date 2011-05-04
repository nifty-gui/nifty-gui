package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * This event is published when the selection of the image select has been changed.
 * @author void
 */
@SuppressWarnings("rawtypes")
public class ImageSelectSelectionChangedEvent implements NiftyEvent {
  private int selectedIndex;

  public ImageSelectSelectionChangedEvent(final int selectedIndex) {
    this.selectedIndex = selectedIndex;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }
}
