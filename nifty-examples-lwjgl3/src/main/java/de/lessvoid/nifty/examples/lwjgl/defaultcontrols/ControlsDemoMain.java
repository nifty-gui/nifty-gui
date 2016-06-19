package de.lessvoid.nifty.examples.lwjgl.defaultcontrols;

import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL3;
import de.lessvoid.nifty.examples.lwjgl.resolution.ResolutionControlLWJGL;

import org.lwjgl.opengl.DisplayMode;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ControlsDemoMain {
  public static void main(@Nonnull final String... args) {
    NiftyExampleLoaderLWJGL3.run(new ControlsDemo<DisplayMode>(new ResolutionControlLWJGL(false)));
  }
}
