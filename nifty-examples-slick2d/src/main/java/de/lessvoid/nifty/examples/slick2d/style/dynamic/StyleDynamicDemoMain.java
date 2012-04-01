package de.lessvoid.nifty.examples.slick2d.style.dynamic;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.style.dynamic.DynamicStyleStartScreen;

/**
 * Demo class to execute the label style demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class StyleDynamicDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new DynamicStyleStartScreen()));
  }
}
