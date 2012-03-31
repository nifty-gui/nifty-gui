package de.lessvoid.nifty.examples.slick2d.mouse;

import de.lessvoid.nifty.examples.mouse.MouseStartScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the mouse demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MouseDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new MouseStartScreen()));
  }
}
