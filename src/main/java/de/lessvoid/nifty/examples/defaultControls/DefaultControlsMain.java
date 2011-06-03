package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public final class DefaultControlsMain {
  public static void main(final String[] args) {
    initLwjgl();
    render(createNifty());
  }

  private static void initLwjgl() {
    if (!LwjglInitHelper.initSubSystems("Nifty Controls Demonstation")) {
      System.exit(0);
    }
  }

  private static Nifty createNifty() {
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());
//    nifty.fromXml("default-controls/controls.xml", "screenButton");
    nifty.fromXml("default-controls/controls.xml", "screenConsole");
    return nifty;
  }

  private static void render(final Nifty nifty) {
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
