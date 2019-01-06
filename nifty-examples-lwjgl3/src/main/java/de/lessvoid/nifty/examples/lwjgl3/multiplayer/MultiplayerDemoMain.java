package de.lessvoid.nifty.examples.lwjgl3.multiplayer;

import de.lessvoid.nifty.examples.lwjgl3.NiftyExampleLoaderLWJGL3;
import de.lessvoid.nifty.examples.multiplayer.StartScreenController;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class MultiplayerDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL3.run(new StartScreenController());
  }
}
