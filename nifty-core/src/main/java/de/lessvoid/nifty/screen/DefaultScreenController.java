package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.Nifty;

import javax.annotation.Nonnull;

/**
 * The DefaultScreenController is attached to a screen when no ScreenController was specified.
 * It does nothing at the moment.
 */
public class DefaultScreenController implements ScreenController {
  Nifty nifty;

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void gotoScreen(@Nonnull final String screenId) {
    nifty.gotoScreen(screenId);
  }
}
