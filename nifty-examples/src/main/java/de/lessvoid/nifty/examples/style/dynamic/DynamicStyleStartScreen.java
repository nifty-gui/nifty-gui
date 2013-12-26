package de.lessvoid.nifty.examples.style.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

public class DynamicStyleStartScreen implements ScreenController, NiftyExample {
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen screenParam) {
    screen = screenParam;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void setNiftyStyle(@Nonnull final String style) {
    screen.findElementById("panel").setStyle(style);
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "style/dynamic/dynamic.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Style Dynamic Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
