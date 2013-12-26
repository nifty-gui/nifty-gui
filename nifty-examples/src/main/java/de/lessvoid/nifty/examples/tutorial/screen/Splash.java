package de.lessvoid.nifty.examples.tutorial.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

public class Splash implements ScreenController {
  private Nifty nifty;

  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    this.nifty = nifty;
  }

  @Override
  public void onStartScreen() {
    nifty.gotoScreen("mainPage");
  }

  @Override
  public void onEndScreen() {
  }
}
