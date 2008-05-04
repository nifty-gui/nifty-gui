package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Controller;

/**
 * ScreenController Interface all screen controllers should support.
 * @author void
 */
public interface ScreenController extends Controller {

  /**
   * bind this ScreenController to a screen.
   * @param nifty nifty
   * @param screen screen
   */
  void bind(Nifty nifty, Screen screen);

  /**
   * called when the screen start but before the onScreenEvent.
   */
  void onStartScreen();

  /**
   * called when all start effects are ended and the screen
   * is ready for interactive manipulation.
   */
  void onStartInteractive();

  /**
   * called when the onEndScreen effects ended and this screen is done.
   */
  void onEndScreen();
}
