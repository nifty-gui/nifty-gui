/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.console;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.examples.LwjglInitHelper.RenderLoopCallback;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * @author void
 */
public final class ConsoleDemoMain {

  /**
   * Prevent instantiation of this class.
   */
  private ConsoleDemoMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Console Demonstation")) {
      System.exit(0);
    }

    // create nifty
    final Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("console/console.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, new RenderLoopCallback() {
      public void process() {
      }
    });
    LwjglInitHelper.destroy();
  }
}