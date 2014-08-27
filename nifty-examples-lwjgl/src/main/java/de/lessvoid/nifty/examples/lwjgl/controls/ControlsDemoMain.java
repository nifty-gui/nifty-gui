package de.lessvoid.nifty.examples.lwjgl.controls;

import de.lessvoid.nifty.examples.controls.ControlsDemoStartScreen;
import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public final class ControlsDemoMain {
  public static void main(@Nonnull final String... args) {
    NiftyExampleLoaderLWJGL.run(new ControlsDemoStartScreen());
  }
}
