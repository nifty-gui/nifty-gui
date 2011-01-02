package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class HelloWorldStartScreen implements ScreenController {

  /** nifty instance. */
  private Nifty nifty;
  private Screen screen;

  /**
   * Bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
  }

  /**
   * on start screen interactive.
   */
  public void onStartScreen() {
  }

  void resize(final int w, final int h) {
    Screen screen = nifty.getCurrentScreen();

    // change root element
    Element root = screen.getRootElement();
    root.setWidth(w);
    root.setHeight(h);
    root.setConstraintWidth(new SizeValue(String.valueOf(w) + "px"));
    root.setConstraintHeight(new SizeValue(String.valueOf(h) + "px"));

    // change all layer element
    for (Element e : screen.getLayerElements()) {
      e.setWidth(w);
      e.setHeight(h);
      e.setConstraintWidth(new SizeValue(String.valueOf(w) + "px"));
      e.setConstraintHeight(new SizeValue(String.valueOf(h) + "px"));
    }

    // make sure all elements of the screen get updated
    screen.layoutLayers();
  } 

  public void enable() {
    resize(300, 300);
  }

  public void disable() {
    screen.findElementByName("panel").disable();
    screen.findElementByName("testButton").disable();
  }

  public void testButtonClick() {
    System.out.println("testButtonClick()");
  }

  public void mouseMove(final Element element, final MouseInputEvent inputEvent) {
  }

  /**
   * on end screen.
   */
  public void onEndScreen() {
  }

  public void bla() {
    nifty.getCurrentScreen().findElementByName("haha").hide();
  }

  public void bla2() {
    nifty.getCurrentScreen().findElementByName("haha").show();
  }

  /**
   * quit method called from the helloworld.xml.
   */
  public final void quit() {
    System.out.println("quit()");
  }
}
