package de.lessvoid.nifty.examples.lwjgl.all;

import de.lessvoid.nifty.examples.all.AllExamples;
import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL3;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class AllDemoMain {
  public static void main(@Nonnull final String... args) {
    NiftyExampleLoaderLWJGL3.run (new AllExamples());
  }
}
