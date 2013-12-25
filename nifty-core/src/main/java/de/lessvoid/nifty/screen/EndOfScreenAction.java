package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.elements.Action;

import javax.annotation.Nonnull;

public class EndOfScreenAction implements Action {
  @Nonnull
  private final Screen screen;

  public EndOfScreenAction(@Nonnull final Screen screen) {
    this.screen = screen;
  }

  @Override
  public void perform() {
    screen.onEndScreenHasEnded();
  }
}