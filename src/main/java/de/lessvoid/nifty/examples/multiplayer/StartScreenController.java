package de.lessvoid.nifty.examples.multiplayer;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Multiplayer.
 * @author void
 */
public class StartScreenController implements ScreenController {

  /** nifty instance. */
  private Nifty nifty;
  
  /** screen. */
  private Screen screen;

  /**
   * Bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
  }

  /**
   * on start screen.
   */
  public final void onStartScreen() {
    nifty.addControl(screen, screen.findElementByName("box-parent"), "multiplayerPanel", "100000");
  }

  /**
   * on start screen interactive.
   */
  public final void onStartInteractive() {
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }

  /**
   * quit method.
   */
  public final void quit() {
    nifty.exit();
  }

}
