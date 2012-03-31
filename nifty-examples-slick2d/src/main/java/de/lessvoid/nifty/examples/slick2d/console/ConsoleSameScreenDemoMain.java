package de.lessvoid.nifty.examples.slick2d.console;

import de.lessvoid.nifty.examples.console.ConsoleSameScreenStartScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the Console on same screen demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ConsoleSameScreenDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new ConsoleSameScreenStartScreen()));
  }
}
