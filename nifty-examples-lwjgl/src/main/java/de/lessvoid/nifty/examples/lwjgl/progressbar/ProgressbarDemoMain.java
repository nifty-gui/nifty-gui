package de.lessvoid.nifty.examples.lwjgl.progressbar;

import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL;
import de.lessvoid.nifty.examples.progressbar.ProgressbarControl;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ProgressbarDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL.run(new ProgressbarControl(), new ProgressBarUpdater());
  }
}
