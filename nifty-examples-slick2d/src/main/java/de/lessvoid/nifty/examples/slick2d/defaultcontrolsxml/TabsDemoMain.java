package de.lessvoid.nifty.examples.slick2d.defaultcontrolsxml;

import de.lessvoid.nifty.examples.defaultcontrolsxml.TabsExample;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the default controls XML &quot;TabGroup&quot; demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TabsDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new TabsExample()));
  }
}
