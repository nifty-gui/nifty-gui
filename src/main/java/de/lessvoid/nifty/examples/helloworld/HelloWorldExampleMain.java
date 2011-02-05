package de.lessvoid.nifty.examples.helloworld;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.controls.button.ButtonControl;
import de.lessvoid.nifty.controls.button.builder.CreateButtonControl;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Hello World.
 * @author void
 */
public final class HelloWorldExampleMain {

  /**
   * Prevent instantiation of this class.
   */
  private HelloWorldExampleMain() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) throws IOException {
    if (!LwjglInitHelper.initSubSystems("Nifty Hello World")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());
    nifty.fromXml("src/main/resources/helloworld/helloworld.xml", "start");

    // get the NiftyMouse interface that gives us access to all mouse cursor related stuff
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    // register/load a mouse cursor (this would be done somewhere at the beginning)
    niftyMouse.registerMouseCursor("mouseId", "src/main/resources/nifty-cursor.png", 0, 0);

    // change the cursor to the one we've loaded before
    niftyMouse.enableMouseCursor("mouseId");

    // we could set the position like so
    niftyMouse.setMousePosition(20, 20);

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
