package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.spi.slick.SlickSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Examples.
 * @author void
 */
public class AllExamplesMain {

  /**
   * file of the main program.
   */
  private static final String ALL_INTRO_XML = "all/intro.xml";

  /**
   * Prevent instantiation of this class.
   */
  private AllExamplesMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Examples")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundDevice()),
        new TimeProvider());
    if (args.length == 1) {
      nifty.fromXml(ALL_INTRO_XML, args[0]);
    } else {
      nifty.fromXml(ALL_INTRO_XML, "start");
    }

    boolean done = false;
    while (!done) {
      LwjglInitHelper.renderLoop(nifty, null);
      done = true;
    }

    LwjglInitHelper.destroy();
  }
}
