package de.lessvoid.nifty.examples.slick2d.dragndrop;

import de.lessvoid.nifty.examples.dragndrop.DragDropScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the Drag'n'Drop demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class DragndropDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new DragDropScreen()));
  }
}
