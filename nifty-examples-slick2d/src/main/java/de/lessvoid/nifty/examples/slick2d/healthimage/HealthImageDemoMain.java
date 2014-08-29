package de.lessvoid.nifty.examples.slick2d.healthimage;

import de.lessvoid.nifty.examples.helloniftybuilder.HelloNiftyBuilderExample;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the health image demonstration.
 *
 *
 */
public class HealthImageDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new HelloNiftyBuilderExample()));
  }
}
