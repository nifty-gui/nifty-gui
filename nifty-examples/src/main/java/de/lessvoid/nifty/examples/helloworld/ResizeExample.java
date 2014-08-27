package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

public class ResizeExample implements ScreenController, NiftyExample {

  public ResizeExample() {
  }

  @Override
  public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "helloworld/resize.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Hello World";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
