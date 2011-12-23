package de.lessvoid.nifty.examples.reload;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public final class ReloadMain {

  private ReloadMain() {
  }

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
    nifty.fromXml("src/main/resources/reload/start.xml", "start");

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
