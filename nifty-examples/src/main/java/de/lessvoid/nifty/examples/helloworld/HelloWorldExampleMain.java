package de.lessvoid.nifty.examples.helloworld;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

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
  public static void main(final String[] args) throws IOException {
    if (!LwjglInitHelper.initSubSystems("Nifty Hello World")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());

    final HelloWorldStartScreen screen = new HelloWorldStartScreen();
    nifty.registerScreenController(screen);

    screen.prepareStart(nifty);

    nifty.fromXml("src/main/resources/helloworld/helloworld.xml", "start");


    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
