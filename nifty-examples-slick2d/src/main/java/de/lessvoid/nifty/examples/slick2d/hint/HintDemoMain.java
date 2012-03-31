package de.lessvoid.nifty.examples.slick2d.hint;

import de.lessvoid.nifty.examples.hint.HintScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the hint demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class HintDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new HintScreen()));
  }
}
