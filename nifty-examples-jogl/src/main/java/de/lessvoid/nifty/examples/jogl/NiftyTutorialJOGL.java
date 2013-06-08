package de.lessvoid.nifty.examples.jogl;

import java.awt.Frame;

import javax.media.opengl.awt.GLCanvas;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;

public class NiftyTutorialJOGL {

  public static void main(String[] args) throws Exception {
    JOGLNiftyRunner.run(args, new Callback() {

      @Override
      public void init(final Nifty nifty, final GLCanvas canvas, final Frame frame) {
        nifty.fromXml("tutorial/tutorial.xml", "start");
      }
    });
  }
}
