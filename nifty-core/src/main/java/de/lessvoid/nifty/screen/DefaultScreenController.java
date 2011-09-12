package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.Nifty;

/**
 * The DefaultScreenController is attached to a screen when no ScreenController was specified.
 * It does nothing at the moment.
 */
public class DefaultScreenController implements ScreenController {
  Nifty nifty;

  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void gotoScreen(final String screenId) {
    nifty.gotoScreen(screenId);
  }
}
