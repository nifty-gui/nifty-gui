package de.lessvoid.nifty.examples.slick2d.reload;

import de.lessvoid.nifty.examples.reload.ReloadScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the reload demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ReloadDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new ReloadScreen()));
  }
}
