package de.lessvoid.nifty.examples.jogl.tutorial;

import de.lessvoid.nifty.examples.jogl.NiftyExampleLoaderJOGL;
import de.lessvoid.nifty.examples.tutorial.TutorialExample;

import javax.annotation.Nonnull;

public class TutorialDemoMain {
  public static void main(@Nonnull String... args) {
    NiftyExampleLoaderJOGL.runWithJOGL(new TutorialExample(), args);
  }
}
