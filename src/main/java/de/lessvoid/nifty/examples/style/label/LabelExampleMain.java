package de.lessvoid.nifty.examples.style.label;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Style Label Example.
 * @author void
 */
public final class LabelExampleMain {

  /**
   * Prevent instantiation of this class.
   */
  private LabelExampleMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Style Label Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundLoader()),
        new TimeProvider());
    nifty.fromXml("style/label/label.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty);
    LwjglInitHelper.destroy();
  }
}
