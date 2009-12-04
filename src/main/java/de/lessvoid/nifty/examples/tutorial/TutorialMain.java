package de.lessvoid.nifty.examples.tutorial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.lwjglslick.render.RenderDeviceLwjgl;
import de.lessvoid.nifty.lwjglslick.sound.SlickSoundDevice;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.TimeProvider;

public final class TutorialMain {

  private TutorialMain() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Tutorial")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundDevice()),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());
    nifty.fromXml("tutorial/tutorial.xml", "page1");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
