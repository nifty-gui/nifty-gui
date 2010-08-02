package de.lessvoid.nifty.examples.localize;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LocalizeTestScreen implements ScreenController {
  public void bind(final Nifty newNifty, final Screen newScreen) {
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public String method1() {
    return "no param";
  }

  public String method2(final String param) {
    return "param: " + param;
  }

  public String sound() {
    return "outro";
  }
}
