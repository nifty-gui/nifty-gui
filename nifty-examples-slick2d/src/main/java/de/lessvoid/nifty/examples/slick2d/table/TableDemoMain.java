package de.lessvoid.nifty.examples.slick2d.table;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.table.TableStartScreen;

/**
 * Demo class to execute the table layout demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TableDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new TableStartScreen()));
  }
}
