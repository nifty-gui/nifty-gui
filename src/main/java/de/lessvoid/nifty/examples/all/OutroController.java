package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Outro implementation for the nifty demo Outro screen.
 * @author void
 */
public class OutroController implements ScreenController {

  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen screen) {
    this.nifty = newNifty;
  }

  public final void onStartScreen() {
    nifty.exit();
  }

  public final void onEndScreen() {
  }
}
