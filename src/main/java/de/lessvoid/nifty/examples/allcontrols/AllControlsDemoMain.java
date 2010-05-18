/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.allcontrols;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.lwjglslick.render.RenderDeviceLwjgl;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public class AllControlsDemoMain {

  public static void main(final String[] args) throws Exception {
    if (!LwjglInitHelper.initSubSystems("Nifty Standard Controls Demonstation")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SlickSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());
    nifty.validateXml("allcontrols/allcontrols.xml");
    nifty.fromXml("allcontrols/allcontrols.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}