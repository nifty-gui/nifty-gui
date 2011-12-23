package de.lessvoid.nifty.examples.textalign;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * Text Align Example.
 * @author void
 */
public final class TextAlignExampleMain {

  /**
   * Prevent instantiation of this class.
   */
  private TextAlignExampleMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Textalign Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("textalign/textalign.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
