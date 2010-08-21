package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.Nifty;

/**
 * The DefaultScreenController is attached to a screen when no ScreenController was specified.
 * It does nothing at the moment.
 */
public class DefaultScreenController implements ScreenController {
  public void bind(Nifty nifty, Screen screen) {
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }
  
}
