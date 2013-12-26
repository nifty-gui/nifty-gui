package de.lessvoid.nifty.examples.resolution;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.lwjgl.opengl.DisplayMode;

public final class ResolutionExampleMain {

  private ResolutionExampleMain() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Screen Resolution")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());

    nifty.registerScreenController(new ResolutionScreen<DisplayMode>(new ResolutionControlLWJGL(false)));
    nifty.fromXml("src/main/resources/resolution/resolution.xml", "start");

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
