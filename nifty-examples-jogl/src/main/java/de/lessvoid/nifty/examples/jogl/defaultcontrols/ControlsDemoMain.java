package de.lessvoid.nifty.examples.jogl.defaultcontrols;

import com.jogamp.newt.opengl.GLWindow;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;
import de.lessvoid.nifty.examples.jogl.NiftyExampleLoaderJOGL;
import de.lessvoid.nifty.examples.jogl.resolution.ResolutionControlJOGL;
import de.lessvoid.nifty.examples.jogl.resolution.ResolutionControlJOGL.Resolution;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ControlsDemoMain {
  public static void main (@Nonnull final String... args) {
    NiftyExampleLoaderJOGL.runWithJOGL(new Callback() {
      @Override
      public void init(@Nonnull Nifty nifty, @Nonnull GLWindow window) {
        NiftyExampleLoaderJOGL.runWithNifty(new ControlsDemo<Resolution>(new ResolutionControlJOGL(window)), nifty);
      }
    }, args);
  }
}
