package de.lessvoid.nifty.examples.multiplayer;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.render.opengl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The MultiplayerExampleMain.
 * @author void
 */
public final class MultiplayerExampleMain {

  /**
   * Prevent instantiation of this class.
   */
  private MultiplayerExampleMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Multiplayer Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundLoader()),
        new TimeProvider(),
        true);
    nifty.fromXml("multiplayer/multiplayer.xml");

    // render
    LwjglInitHelper.renderLoop(nifty);
    LwjglInitHelper.destroy();
  }
}
