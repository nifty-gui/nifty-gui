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

  private int id = 10000;

  /**
   * Bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    addPanel();
  }

  /**
   * add panel.
   */
  public void addPanel() {
    nifty.addControl(screen, screen.findElementByName("box-parent"), "multiplayerPanel", "" + id++);
  }

  /**
   * on start screen interactive.
   */
  public final void onStartScreen() {
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
