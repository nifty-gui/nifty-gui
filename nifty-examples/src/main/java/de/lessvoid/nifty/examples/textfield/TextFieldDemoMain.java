/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

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
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("textfield/textfield.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}