package de.lessvoid.nifty.examples.slick2d.all;

import de.lessvoid.nifty.examples.all.AllExamplesMain;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the nifty-gui demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class AllDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new AllExamplesMain()));
  }
}
