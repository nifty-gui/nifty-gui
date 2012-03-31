package de.lessvoid.nifty.examples.test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class TestScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

  public void onStartScreen() {
  }

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

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "test/test-popup.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty General Test";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
