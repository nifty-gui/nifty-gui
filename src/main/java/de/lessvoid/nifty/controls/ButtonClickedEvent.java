package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class ButtonClickedEvent implements NiftyEvent<Void> {
  private Button button;

  public ButtonClickedEvent(final Button button) {
    this.button = button;
  }

  public Button getButton() {
    return button;
  }
}
