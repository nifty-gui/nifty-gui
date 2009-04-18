package de.lessvoid.nifty.examples.scroll;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.scrollpanel.ScrollPanel;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScrollDemoStartScreen implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    Element myScrollStuff = screen.findElementByName("myScrollStuff");
    if (myScrollStuff != null) {
      for (int i=0; i<100; i++) {
        addLabel(myScrollStuff, "hello " + i);
      }
      screen.layoutLayers();

      ScrollPanel scrollPanel = screen.findControl("scrollbarPanel", ScrollPanel.class);
      scrollPanel.initializeScrollPanel(screen, 0, myScrollStuff.getHeight() / 100.f);
    }
  }

  public void onStartScreen() {
  }

  private void addLabel(final Element myScrollStuff, final String text) {
    LabelCreator label = new LabelCreator(text);
    label.create(nifty, screen, myScrollStuff);
  }

  public void onEndScreen() {
  }

  public void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }
}
