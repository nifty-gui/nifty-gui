package de.lessvoid.nifty.examples.slick2d.style.label;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.style.label.LabelStartScreen;

/**
 * Demo class to execute the label style demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class StyleLabelDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new LabelStartScreen()));
  }
}
