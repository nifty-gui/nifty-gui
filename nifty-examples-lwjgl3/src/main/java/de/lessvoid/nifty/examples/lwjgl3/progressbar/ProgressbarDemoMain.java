package de.lessvoid.nifty.examples.lwjgl3.progressbar;

import de.lessvoid.nifty.examples.lwjgl3.NiftyExampleLoaderLWJGL3;
import de.lessvoid.nifty.examples.progressbar.ProgressbarControl;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ProgressbarDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL3.run(new ProgressbarControl(), new ProgressBarUpdater());
  }
}
