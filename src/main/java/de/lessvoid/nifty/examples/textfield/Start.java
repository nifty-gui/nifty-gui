package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * MainMenu.
 * @author void
 *
 */
public class Start implements ScreenController {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * screen.
   */
  private Screen screen;

  /**
   * bind this ScreenController to a screen.
   * @param newRenderDevice RenderDevice
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(
      final Nifty newNifty,
      final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
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
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }

  /**
   * next.
   */
  public final void next() {
  }

}
