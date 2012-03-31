package de.lessvoid.nifty.examples.helloworld;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class ResizeMain implements ScreenController, NiftyExample {

  public ResizeMain() {
  }

  public static void main(final String[] args) throws IOException {
    if (!LwjglInitHelper.initSubSystems("Nifty Hello World")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("src/main/resources/helloworld/resize.xml", "start");

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  @Override
  public void bind(Nifty nifty, Screen screen) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "helloworld/resize.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Hello World";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
