package de.lessvoid.nifty.examples.table;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for TableStartScreen.
 * @author void
 */
public class TableStartScreen implements ScreenController {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }
}
