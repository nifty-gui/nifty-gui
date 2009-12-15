package de.lessvoid.nifty.examples.tutorial.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Splash implements ScreenController {
  private Nifty nifty;

  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
  }

  public void onStartScreen() {
	  nifty.gotoScreen("mainPage");
  }

  public void onEndScreen() {
  }
}
