package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class ButtonReleasedEvent implements NiftyEvent {
  private Button button;

  public ButtonReleasedEvent (final Button button) {
    this.button = button;
  }

  public Button getButton() {
    return button;
  }
}
