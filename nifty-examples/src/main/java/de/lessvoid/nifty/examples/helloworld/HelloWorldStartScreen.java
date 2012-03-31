package de.lessvoid.nifty.examples.helloworld;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class HelloWorldStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "helloworld/helloworld.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Hello World";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // get the NiftyMouse interface that gives us access to all mouse cursor related stuff
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    try {
    // register/load a mouse cursor (this would be done somewhere at the beginning)
    niftyMouse.registerMouseCursor("mouseId", "nifty-cursor.png", 0, 0);

    // change the cursor to the one we've loaded before
    niftyMouse.enableMouseCursor("mouseId");
    } catch (IOException e) {
      System.err.println("Loading the mouse cursor failed.");
    }

    // we could set the position like so
    niftyMouse.setMousePosition(20, 20);
  }
}
