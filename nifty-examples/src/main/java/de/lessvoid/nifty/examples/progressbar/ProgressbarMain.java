package de.lessvoid.nifty.examples.progressbar;

import java.util.Date;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.examples.LwjglInitHelper.RenderLoopCallback;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class ProgressbarMain {

  private ProgressbarMain() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Hello World")) {
      System.exit(0);
    }

    // create nifty
    final Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("progressbar/progressbar.xml", "start");

    // render
    LwjglInitHelper.renderLoop(nifty, new RenderLoop(nifty));
    LwjglInitHelper.destroy();
  }

  private static final class RenderLoop implements RenderLoopCallback {
    private final Nifty nifty;
    private int progress = 0;
    private long start = new Date().getTime();

    private RenderLoop(final Nifty nifty) {
      this.nifty = nifty;
    }

    @Override
    public void process() {
      long now = new Date().getTime();
      if (now - start > 50) { // add one percent every 50 ms
        start = now;

        progress++;
        nifty.getScreen("start").findControl("my-progress", ProgressbarControl.class).setProgress(progress / 100.0f);

        if (progress >= 100) {
          System.out.println("done");
        }
      }
    }
  }
}