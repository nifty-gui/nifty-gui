package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

public class ButtonReleasedEvent implements NiftyEvent {
  @Nonnull
  private final Button button;

  public ButtonReleasedEvent(@Nonnull final Button button) {
    this.button = button;
  }

  @Nonnull
  public Button getButton() {
    return button;
  }
}
