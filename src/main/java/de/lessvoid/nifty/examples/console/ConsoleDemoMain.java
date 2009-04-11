/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.console;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.examples.LwjglInitHelper.RenderLoopCallback;
import de.lessvoid.nifty.lwjglslick.input.LwjglInputSystem;
import de.lessvoid.nifty.lwjglslick.render.RenderDeviceLwjgl;
import de.lessvoid.nifty.lwjglslick.sound.SlickSoundDevice;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.TimeProvider;

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
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundDevice()),
        new LwjglInputSystem(), new TimeProvider());
    nifty.fromXml("console/console.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, new RenderLoopCallback() {
      public void process() {
      }
    });
    LwjglInitHelper.destroy();
  }
}