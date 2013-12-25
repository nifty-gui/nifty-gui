package de.lessvoid.nifty.examples.jogl;

import com.jogamp.newt.opengl.GLWindow;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;

import javax.annotation.Nonnull;

public class NiftyTutorialJOGL {

  public static void main(@Nonnull String[] args) throws Exception {
    JOGLNiftyRunner.run(args, new Callback() {

      @Override
      public void init(@Nonnull final Nifty nifty, final GLWindow window) {
        nifty.fromXml("tutorial/tutorial.xml", "start");
      }
    });
  }
}
