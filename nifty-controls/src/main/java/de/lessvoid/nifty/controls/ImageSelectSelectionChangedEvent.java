package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * This event is published when the selection of the image select has been changed.
 *
 * @author void
 */
public class ImageSelectSelectionChangedEvent implements NiftyEvent {
  @Nonnull
  private final ImageSelect imageSelect;
  private final int selectedIndex;

  public ImageSelectSelectionChangedEvent(@Nonnull final ImageSelect imageSelect, final int selectedIndex) {
    this.imageSelect = imageSelect;
    this.selectedIndex = selectedIndex;
  }

  @Nonnull
  public ImageSelect getImageSelect() {
    return imageSelect;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }
}
