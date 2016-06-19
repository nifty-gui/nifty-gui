package de.lessvoid.nifty.examples.lwjgl.console;

import de.lessvoid.nifty.examples.console.ConsoleSameScreenStartScreen;
import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL3;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public final class ConsoleSameScreenDemoMain {
  public static void main(@Nonnull final String... args) {
    NiftyExampleLoaderLWJGL3.run(new ConsoleSameScreenStartScreen());
  }
}