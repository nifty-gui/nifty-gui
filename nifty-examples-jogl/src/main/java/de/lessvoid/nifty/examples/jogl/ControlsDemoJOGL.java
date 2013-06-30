package de.lessvoid.nifty.examples.jogl;

import com.jogamp.newt.opengl.GLWindow;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;
import de.lessvoid.nifty.examples.jogl.ResolutionControlJOGL.Resolution;

public class ControlsDemoJOGL {

  public static void main(String[] args) throws Exception {
    JOGLNiftyRunner.run(args, new Callback() {

      @Override
      public void init(final Nifty nifty, final GLWindow window) {
        ControlsDemo<Resolution> demo;
          demo = new ControlsDemo<Resolution>(new ResolutionControlJOGL(window));
          demo.prepareStart(nifty);
        nifty.gotoScreen("start");
      }
    });
  }
}
