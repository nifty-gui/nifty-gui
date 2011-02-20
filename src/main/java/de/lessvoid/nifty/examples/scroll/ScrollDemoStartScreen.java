package de.lessvoid.nifty.examples.scroll;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.controls.label.builder.CreateLabelControl;
import de.lessvoid.nifty.controls.scrollpanel.ScrollPanelControl;
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

      ScrollPanelControl scrollPanel = screen.findControl("scrollbarPanel", ScrollPanelControl.class);
      scrollPanel.setUp(0, myScrollStuff.getHeight() / 100.f, 0, myScrollStuff.getHeight() / 10.f, AutoScroll.OFF);
      scrollPanel.setVerticalPos(0.0f);
    }
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  private void addLabel(final Element myScrollStuff, final String text) {
    CreateLabelControl label = new CreateLabelControl(text);
    label.create(nifty, screen, myScrollStuff);
  }
}
