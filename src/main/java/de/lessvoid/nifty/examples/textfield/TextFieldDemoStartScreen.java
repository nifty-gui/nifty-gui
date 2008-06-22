package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * MainMenu.
 * @author void
 *
 */
public class TextFieldDemoStartScreen implements ScreenController {

  /**
   * the nifty.
   */
  private Nifty nifty;
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
    screen = newScreen;
    nifty = newNifty;
  }

  /**
   * on start screen.
   */
  public final void onStartScreen() {
    screen.findElementByName("backButton").setStyle("huhu");
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
   * back.
   */
  public final void back() {
    nifty.exit();
  }

}
