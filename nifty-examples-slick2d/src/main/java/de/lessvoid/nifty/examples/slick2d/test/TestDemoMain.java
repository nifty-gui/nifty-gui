package de.lessvoid.nifty.examples.slick2d.test;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.test.TestScreen;

/**
 * Demo class to execute the general test.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TestDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new TestScreen()));
  }
}
