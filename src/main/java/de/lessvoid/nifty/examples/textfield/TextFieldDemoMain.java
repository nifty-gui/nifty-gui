/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.spi.slick.SlickSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * @author void
 */
public class TextFieldDemoMain {

  /**
   * Prevent instantiation of this class.
   */
  private TextFieldDemoMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Textfield Demonstation")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundDevice()),
        new TimeProvider());
    nifty.fromXml("textfield/textfield.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}