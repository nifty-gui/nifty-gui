package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * SplasScreen implementation for the nifty demo splash screen.
 * @author void
 */
public class SplashController2 implements ScreenController {

  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen screen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
    nifty.gotoScreen("menu");
  }

  public void onEndScreen() {
  }
}
