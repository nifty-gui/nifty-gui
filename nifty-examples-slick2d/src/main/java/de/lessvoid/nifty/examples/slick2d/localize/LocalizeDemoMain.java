package de.lessvoid.nifty.examples.slick2d.localize;

import de.lessvoid.nifty.examples.localize.LocalizeTestScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the localization demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class LocalizeDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new LocalizeTestScreen()));
  }
}
