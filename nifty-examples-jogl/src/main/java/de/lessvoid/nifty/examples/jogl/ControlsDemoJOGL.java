package de.lessvoid.nifty.examples.jogl;

import java.awt.Frame;

import javax.media.opengl.awt.GLCanvas;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;
import de.lessvoid.nifty.examples.jogl.ResolutionControlJOGL.Resolution;

public class ControlsDemoJOGL {

  public static void main(String[] args) throws Exception {
    JOGLNiftyRunner.run(args, new Callback() {

      @Override
      public void init(final Nifty nifty, final GLCanvas canvas, final Frame frame) {
        ControlsDemo<Resolution> demo = new ControlsDemo<Resolution>(new ResolutionControlJOGL(canvas, frame));
        demo.prepareStart(nifty);
        nifty.gotoScreen("start");
      }
    });
  }
}
