package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * SplasScreen implementation for the nifty demo splash screen.
 * @author void
 *
 */
public class SplashController implements ScreenController {

  /**
   * the nifty instance.
   */
  private Nifty nifty;

  /**
   * 
   */
  public void bind(Nifty newNifty, Screen screen) {
    this.nifty = newNifty;
  }

  /**
   * on start screen.
   */
  public final void onStartScreen() {
  }

  /**
   * just goto the next screen.
   */
  public final void onStartInteractive() {
    nifty.gotoScreen("menu");
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }
}
