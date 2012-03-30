package de.lessvoid.nifty.examples.support.help4081311;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Controller implements ScreenController {
  @Override
  public void bind(Nifty nifty, Screen screen) {
    // Leave empty.
  }

  @Override
  public void onEndScreen() {
    // Leave empty.
  }

  @Override
  public void onStartScreen() {
    // Leave empty.
  }

  public void hello() {
    System.out.println("Hello World!");
  }
}