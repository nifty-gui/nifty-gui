package de.lessvoid.nifty.examples.mouse;

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
public final class MouseExampleMain {

  /**
   * Prevent instantiation of this class.
   */
  private MouseExampleMain() {
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
    
    MouseStartScreen startScreen = new MouseStartScreen();
    nifty.registerScreenController(startScreen);
    nifty.fromXml("src/main/resources/mouse/mouse.xml", "start");

    startScreen.prepareStart(nifty);

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
