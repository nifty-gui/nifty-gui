package de.lessvoid.nifty.examples.lwjgl.resolution;

import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL;
import de.lessvoid.nifty.examples.resolution.ResolutionScreen;

import org.lwjgl.opengl.DisplayMode;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ResolutionDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL.run(new ResolutionScreen<DisplayMode>(new ResolutionControlLWJGL(false)));
  }
}
