package de.lessvoid.nifty.examples.style.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
import de.lessvoid.nifty.tools.TimeProvider;

public class DynamicStyleExampleMain {

  private DynamicStyleExampleMain() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Style Dynamic Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundLoader()),
        new TimeProvider());
    nifty.fromXml("style/dynamic/dynamic.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
