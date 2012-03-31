package de.lessvoid.nifty.examples.slick2d.scroll;

import de.lessvoid.nifty.examples.scroll.ScrollDemoStartScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the scroll demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ScrollDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new ScrollDemoStartScreen()));
  }
}
