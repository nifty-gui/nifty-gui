package de.lessvoid.nifty.examples.slick2d.falloff;

import de.lessvoid.nifty.examples.falloff.FalloffExample;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the falloff demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FalloffDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new FalloffExample()));
  }
}
