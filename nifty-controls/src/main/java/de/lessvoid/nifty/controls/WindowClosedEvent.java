package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

public class WindowClosedEvent implements NiftyEvent {
  @Nonnull
  private final Window window;
  private final boolean hidden;

  public WindowClosedEvent(@Nonnull final Window window, final boolean hidden) {
    this.window = window;
    this.hidden = hidden;
  }

  @Nonnull
  public Window getWindow() {
    return window;
  }

  public boolean isHidden() {
    return hidden;
  }
}
