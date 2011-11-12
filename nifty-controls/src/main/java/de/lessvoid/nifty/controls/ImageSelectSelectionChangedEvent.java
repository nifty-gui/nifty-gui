package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * This event is published when the selection of the image select has been changed.
 * @author void
 */
@SuppressWarnings("rawtypes")
public class ImageSelectSelectionChangedEvent implements NiftyEvent {
  private ImageSelect imageSelect;
  private int selectedIndex;

  public ImageSelectSelectionChangedEvent(final ImageSelect imageSelect, final int selectedIndex) {
    this.imageSelect = imageSelect;
    this.selectedIndex = selectedIndex;
  }

  public ImageSelect getImageSelect() {
    return imageSelect;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }
}
