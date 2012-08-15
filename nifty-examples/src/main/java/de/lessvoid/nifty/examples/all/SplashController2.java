package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * SplasScreen implementation for the nifty demo splash screen.
 * @author void
 */
public class SplashController2 implements ScreenController, KeyInputHandler {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen screen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.gotoScreen("menu");
  }

  public void onEndScreen() {
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Escape) {
      nifty.setAlternateKey("exit");
      nifty.gotoScreen("menu");
      return true;
    }
    return false;
  }
}
