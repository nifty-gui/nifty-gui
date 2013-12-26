package de.lessvoid.nifty.examples.scroll;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.scrollpanel.ScrollPanelControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

public class ScrollDemoStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    Element myScrollStuff = screen.findElementById("myScrollStuff");
    if (myScrollStuff != null) {
      for (int i = 0; i < 100; i++) {
        addLabel(myScrollStuff, "hello " + i);
      }
      screen.layoutLayers();

      ScrollPanelControl scrollPanel = screen.findControl("scrollbarPanel", ScrollPanelControl.class);
      scrollPanel.setUp(0, myScrollStuff.getHeight() / 100.f, 0, myScrollStuff.getHeight() / 10.f, AutoScroll.OFF);
      scrollPanel.setVerticalPos(0.0f);
    }
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  private void addLabel(@Nonnull final Element myScrollStuff, @Nonnull final String text) {
    new LabelBuilder(NiftyIdCreator.generate(), text).build(nifty, screen, myScrollStuff);
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "scroll/scroll.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Scrolling Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
