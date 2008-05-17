package de.lessvoid.nifty.examples;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.opengl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Examples.
 * @author void
 */
public final class AllExamplesMain {

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
        new SoundSystem(new SlickSoundLoader()),
        new TimeProvider(),
        true);
    nifty.fromXml("intro/intro.xml", "start");

    boolean done = false;
    while(!done) {
      LwjglInitHelper.renderLoop(nifty);
    
      // catch the end of the different single demo and loop them back to the menu
      if (nifty.isActive("helloworld/helloworld.xml", "start")) {
        nifty.fromXml("intro/intro.xml", "menu");
      } else if (nifty.isActive("textfield/textfield.xml", "start")) {
        nifty.fromXml("intro/intro.xml", "menu");
      } else {
        done = true;
      }
    }

    LwjglInitHelper.destroy();
  }
}
