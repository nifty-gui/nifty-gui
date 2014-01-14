package de.lessvoid.nifty.examples.jogl;

import com.jogamp.newt.opengl.GLWindow;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * Loads & runs any {@link de.lessvoid.nifty.examples.NiftyExample} using Nifty and/or JOGL.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class NiftyExampleLoaderJOGL {
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyExampleLoaderJOGL.class.getName());

  /**
   * This is the standard, simple way to run a {@link de.lessvoid.nifty.examples.NiftyExample}. Just instantiate your
   * example and pass it along with your main method's arguments to this method. This method will use JOGL to run your
   * example, automatically creating & initializing a JOGL application for you before running the example.
   *
   * @param example The {@link de.lessvoid.nifty.examples.NiftyExample} to run.
   * @param args The arguments from your application's main method.
   */
  public static void runWithJOGL(@Nonnull final NiftyExample example, @Nonnull final String... args) {
    runWithJOGL(new Callback() {
      @Override
      public void init(@Nonnull Nifty nifty, @Nonnull GLWindow window) {
        runWithNifty(example, nifty);
      }
    }, args);
  }

  /**
   * This is the advanced way to run a {@link de.lessvoid.nifty.examples.NiftyExample} for when you need to pass a
   * {@link com.jogamp.newt.opengl.GLWindow} to your example before running it. You can create a custom
   * {@link de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback}, pass the GLWindow to your example, and then call
   * {@link #runWithNifty(de.lessvoid.nifty.examples.NiftyExample, de.lessvoid.nifty.Nifty)} from within your custom
   * callback method to actually run the example with a valid GLWindow. For an example (no pun intended), see
   * {@link de.lessvoid.nifty.examples.jogl.defaultcontrols.ControlsDemoMain}. You could also use your custom callback
   * to do other things, such as passing a Nifty instance to your example, or performing some other kind of
   * initialization. This method will use JOGL to run your custom callback, automatically creating & initializing a
   * JOGL application for you before running the example.
   *
   * @see #runWithNifty(de.lessvoid.nifty.examples.NiftyExample, de.lessvoid.nifty.Nifty)
   *
   * @param callback Your custom {@link de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback} implementation.
   * @param args The arguments from your application's main method.
   */
  public static void runWithJOGL(@Nonnull final Callback callback, @Nonnull final String... args) {
    try {
      JOGLNiftyRunner.run(args, callback);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Unable to run Nifty example!", e);
    }
  }

  /**
   * Directly run a {@link de.lessvoid.nifty.examples.NiftyExample} without JOGL. Since you must have a valid Nifty
   * instance, this is useful inside of a custom {@link de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback} in
   * conjunction with {@link #runWithJOGL(de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback, String...)}, since
   * your custom Callback method will receive a valid Nifty instance. For an example (no pun intended), see
   * {@link de.lessvoid.nifty.examples.jogl.defaultcontrols.ControlsDemoMain}. This method will use Nifty to run your
   * example, without creating or initializing a JOGL application first. It is up to you to create & initialize your
   * own JOGL application, either by using
   * {@link #runWithJOGL(de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback, String...)} or by writing your own
   * custom JOGL application code.
   *
   * @see #runWithJOGL(de.lessvoid.nifty.examples.jogl.JOGLNiftyRunner.Callback, String...)
   *
   * @param example The {@link de.lessvoid.nifty.examples.NiftyExample} to run.
   * @param nifty The Nifty instance to use to run this example.
   */
  public static void runWithNifty(@Nonnull final NiftyExample example, @Nonnull final Nifty nifty) {
    try {
      example.prepareStart(nifty);
      if (example.getMainXML() != null) {
        nifty.fromXml(example.getMainXML(), example.getStartScreen());
      } else {
        nifty.gotoScreen(example.getStartScreen());
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Unable to run Nifty example!", e);
    }
  }
}
