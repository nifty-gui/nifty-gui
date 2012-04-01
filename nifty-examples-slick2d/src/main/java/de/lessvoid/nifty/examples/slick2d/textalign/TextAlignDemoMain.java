package de.lessvoid.nifty.examples.slick2d.textalign;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.textalign.TextAlignStartScreen;

/**
 * Demo class to execute the text align demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TextAlignDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new TextAlignStartScreen()));
  }
}
