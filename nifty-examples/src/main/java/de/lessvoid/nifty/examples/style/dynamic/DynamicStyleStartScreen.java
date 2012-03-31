package de.lessvoid.nifty.examples.style.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class DynamicStyleStartScreen implements ScreenController, NiftyExample {
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen screenParam) {
    screen = screenParam;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void setNiftyStyle(final String style) {
    screen.findElementByName("panel").setStyle(style);
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "style/dynamic/dynamic.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Style Dynamic Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
