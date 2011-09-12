package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.Nifty;

/**
 * A ScreenController that does absolutly nothing :).
 * @author void
 */
public class NullScreenController implements ScreenController {

  /**
   * bind.
   * @param nifty nifty
   * @param screen screen
   */
  public void bind(final Nifty nifty, final Screen screen) {
  }

  /**
   * on start screen.
   */
  public void onStartScreen() {
  }

  /**
   * on end screen.
   */
  public void onEndScreen() {
  }
}
