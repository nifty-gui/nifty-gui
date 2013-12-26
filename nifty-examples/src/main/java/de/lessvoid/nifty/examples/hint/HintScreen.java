package de.lessvoid.nifty.examples.hint;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

public class HintScreen implements ScreenController, NiftyExample {
  private Nifty nifty;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    this.nifty = newNifty;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "hint/hint.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Hint Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
