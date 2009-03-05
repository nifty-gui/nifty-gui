package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class SplashController implements ScreenController {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen screen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
    nifty.gotoScreen("start2");
  }

  public void onEndScreen() {
  }
}
