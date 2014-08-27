package de.lessvoid.nifty.examples.slick2d.tutorial;

import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.tutorial.TutorialExample;

 /**
 * Demo class to execute the tutorial demonstration.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class TutorialDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new TutorialExample()));
  }
}
