package de.lessvoid.nifty.examples.healthimage;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public final class HealthImageMain {

  private HealthImageMain() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Health Image Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());

    final HealthImageScreen screen = new HealthImageScreen();
    nifty.registerScreenController(screen);

    screen.prepareStart(nifty);

    nifty.fromXml("src/main/resources/healthimage/healthimage.xml", "start");


    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
