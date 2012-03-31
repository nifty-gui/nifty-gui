package de.lessvoid.nifty.examples.slick2d.multiplayer;

import de.lessvoid.nifty.examples.multiplayer.StartScreenController;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the multiplayer demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MultiplayerDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new StartScreenController()));
  }
}
