package de.lessvoid.nifty.examples.localize;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public final class LocalizeTestMain {

  private LocalizeTestMain() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Localize Test")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
//    nifty.setLocale(Locale.US);

    Properties props = new Properties();
    props.put("void", ":)");
    nifty.setGlobalProperties(props);

    nifty.fromXml("localize/localize.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
