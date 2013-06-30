package de.lessvoid.nifty.examples.jogl;

import com.jogamp.newt.opengl.GLWindow;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;

public class NiftyTutorialJOGL {

  public static void main(String[] args) throws Exception {
    JOGLNiftyRunner.run(args, new Callback() {

      @Override
      public void init(final Nifty nifty, final GLWindow window) {
        nifty.fromXml("tutorial/tutorial.xml", "start");
      }
    });
  }
}
