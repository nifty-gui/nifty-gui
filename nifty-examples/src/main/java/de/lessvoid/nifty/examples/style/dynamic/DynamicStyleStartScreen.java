package de.lessvoid.nifty.examples.style.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class DynamicStyleStartScreen implements ScreenController {
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen screenParam) {
    screen = screenParam;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void setNiftyStyle(final String style) {
    screen.findElementByName("panel").setStyle(style);
  }
}
