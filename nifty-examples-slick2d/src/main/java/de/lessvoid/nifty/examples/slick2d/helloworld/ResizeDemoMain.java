package de.lessvoid.nifty.examples.slick2d.helloworld;

import de.lessvoid.nifty.examples.helloworld.ResizeMain;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the resize demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ResizeDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new ResizeMain()));
  }
}
