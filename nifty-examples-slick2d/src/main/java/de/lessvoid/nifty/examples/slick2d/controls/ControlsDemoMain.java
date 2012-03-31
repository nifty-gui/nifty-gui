package de.lessvoid.nifty.examples.slick2d.controls;

import de.lessvoid.nifty.examples.controls.ControlsDemoStartScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the Controls demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ControlsDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new ControlsDemoStartScreen()));
  }
}
