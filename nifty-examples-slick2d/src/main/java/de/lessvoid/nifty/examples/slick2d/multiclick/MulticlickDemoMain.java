package de.lessvoid.nifty.examples.slick2d.multiclick;

import de.lessvoid.nifty.examples.multiclick.MultiClickExample;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 /**
 * Demo class to execute the multiclick demonstration.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class MulticlickDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new MultiClickExample()));
  }
}
