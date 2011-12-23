package de.lessvoid.nifty.examples.menu;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * The Nifty Menu Hello World.
 * @author void
 */
public final class MenuExampleMain {
  private MenuExampleMain() {
  }

  public static void main(final String[] args) throws IOException {
    if (!LwjglInitHelper.initSubSystems("Nifty Menu")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("src/main/resources/menu/menu.xml", "start");

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
