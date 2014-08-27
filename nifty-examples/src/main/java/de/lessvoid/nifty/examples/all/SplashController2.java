package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * @author void
 */
public class SplashController2 implements ScreenController, KeyInputHandler {
  private Nifty nifty;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen screen) {
    this.nifty = newNifty;
  }

  @Override
  public void onStartScreen() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.gotoScreen("menu");
  }

  @Override
  public void onEndScreen() {
  }

  @Override
  public boolean keyEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Escape) {
      nifty.setAlternateKey("exit");
      nifty.gotoScreen("menu");
      return true;
    }
    return false;
  }
}
