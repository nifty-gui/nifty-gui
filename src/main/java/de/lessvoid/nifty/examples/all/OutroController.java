package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Outro implementation for the nifty demo Outro screen.
 * @author void
 *
 */
public class OutroController implements ScreenController {

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
    nifty.exit();
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }
}
