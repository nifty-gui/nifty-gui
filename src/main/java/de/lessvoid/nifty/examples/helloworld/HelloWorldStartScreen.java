package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class HelloWorldStartScreen implements ScreenController {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }
}
