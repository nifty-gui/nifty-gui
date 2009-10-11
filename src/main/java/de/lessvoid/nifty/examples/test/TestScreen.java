package de.lessvoid.nifty.examples.test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class TestScreen implements ScreenController {
  private Nifty nifty;

  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }
}
