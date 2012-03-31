package de.lessvoid.nifty.examples.slick2d.helloniftybuilder;

import de.lessvoid.nifty.examples.helloniftybuilder.HelloNiftyBuilderExampleMain;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the hello world builder demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class HelloNiftyBuilderDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new HelloNiftyBuilderExampleMain()));
  }
}
