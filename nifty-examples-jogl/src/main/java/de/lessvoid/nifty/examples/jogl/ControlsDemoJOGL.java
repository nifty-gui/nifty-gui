package de.lessvoid.nifty.examples.jogl;

import com.jogamp.newt.opengl.GLWindow;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;
import de.lessvoid.nifty.examples.jogl.ResolutionControlJOGL.Resolution;

import javax.annotation.Nonnull;

public class ControlsDemoJOGL {

  public static void main(@Nonnull String[] args) throws Exception {
    JOGLNiftyRunner.run(args, new Callback() {

      @Override
      public void init(@Nonnull final Nifty nifty, final GLWindow window) {
        ControlsDemo<Resolution> demo;
        demo = new ControlsDemo<Resolution>(new ResolutionControlJOGL(window));
        demo.prepareStart(nifty);
        nifty.gotoScreen("start");
      }
    });
  }
}
