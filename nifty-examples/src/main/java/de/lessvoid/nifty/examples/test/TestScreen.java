package de.lessvoid.nifty.examples.test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

public class TestScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void popup() {
    Element element = nifty.createPopup("popupExit");
    nifty.showPopup(screen, element.getId(), null);
  }

  public void popupExit(final String exit) {
    if ("yes".equals(exit)) {
      Element element = nifty.createPopup("popupExit");
      nifty.showPopup(screen, element.getId(), null);
    } else if ("no".equals(exit)) {
      nifty.closePopup(screen.getTopMostPopup().getId(), null);
    }
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "test/test-popup.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty General Test";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
