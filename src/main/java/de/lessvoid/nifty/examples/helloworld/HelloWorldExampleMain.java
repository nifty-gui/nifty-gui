package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Hello World.
 * @author void
 */
public final class HelloWorldExampleMain {

  /**
   * Prevent instantiation of this class.
   */
  private HelloWorldExampleMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Hello World")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundLoader()),
        new TimeProvider());
    nifty.fromXml("helloworld/helloworld.xml");

    // render
    LwjglInitHelper.renderLoop(nifty);
    LwjglInitHelper.destroy();
  }
}
