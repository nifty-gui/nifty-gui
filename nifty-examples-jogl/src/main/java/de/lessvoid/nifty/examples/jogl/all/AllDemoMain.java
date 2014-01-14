package de.lessvoid.nifty.examples.jogl.all;

import de.lessvoid.nifty.examples.all.AllExamplesMain;
import de.lessvoid.nifty.examples.jogl.NiftyExampleLoaderJOGL;

import javax.annotation.Nonnull;

public class AllDemoMain {
  public static void main(@Nonnull String... args) {
    NiftyExampleLoaderJOGL.runWithJOGL(new AllExamplesMain(), args);
  }
}
