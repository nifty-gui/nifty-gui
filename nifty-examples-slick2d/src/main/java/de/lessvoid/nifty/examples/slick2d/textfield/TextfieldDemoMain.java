package de.lessvoid.nifty.examples.slick2d.textfield;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.textfield.TextFieldDemoStartScreen;

/**
 * Demo class to execute the text field demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class TextfieldDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new TextFieldDemoStartScreen()));
  }
}
