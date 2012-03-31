package de.lessvoid.nifty.examples.hint;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class HintScreen implements ScreenController, NiftyExample {
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
    return "hint/hint.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Hint Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
