package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.Nifty;

/**
 * ScreenController Interface all screen controllers should support.
 * @author void
 */
public interface ScreenController {
  /**
   * Bind this ScreenController to a screen. This happens
   * right before the onStartScreen STARTED and only exactly once for a screen!
   * @param nifty nifty
   * @param screen screen
   */
  void bind(Nifty nifty, Screen screen);

  /**
   * called right after the onStartScreen event ENDED.
   */
  void onStartScreen();

  /**
   * called right after the onEndScreen event ENDED.
   */
  void onEndScreen();
}
