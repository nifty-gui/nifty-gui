package de.lessvoid.nifty.examples.lwjgl.multiplayer;

import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL;
import de.lessvoid.nifty.examples.multiplayer.StartScreenController;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class MultiplayerDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL.run(new StartScreenController());
  }
}
