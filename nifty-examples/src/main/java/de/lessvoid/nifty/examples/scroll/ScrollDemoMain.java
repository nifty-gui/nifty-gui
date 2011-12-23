/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.scroll;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public final class ScrollDemoMain {

  private ScrollDemoMain() {
  }

  public static void main(final String[] args) {
    initLwjgl();
    render(createNifty());
  }

  private static void initLwjgl() {
    if (!LwjglInitHelper.initSubSystems("Nifty Scrolling Demonstation")) {
      System.exit(0);
    }
  }

  private static Nifty createNifty() {
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("scroll/scroll.xml", "start");
    return nifty;
  }

  private static void render(final Nifty nifty) {
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
