package de.lessvoid.nifty.examples.slick2d.allcontrols;

import de.lessvoid.nifty.examples.allcontrols.AllControlsDemoStartScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the all controls demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class AllControlsDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new AllControlsDemoStartScreen()));
  }
}
