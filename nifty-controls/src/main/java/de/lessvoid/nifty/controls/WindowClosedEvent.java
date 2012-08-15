package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class WindowClosedEvent implements NiftyEvent {
  private final Window window;
  private final boolean hidden;

  public WindowClosedEvent(final Window window, final boolean hidden) {
    this.window = window;
    this.hidden = hidden;
  }

  public Window getWindow() {
    return window;
  }

  public boolean isHidden() {
    return hidden;
  }
}
