package de.lessvoid.nifty.examples.multiplayer;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * A BoxThing.
 * @author void
 */
public class MultiplayerPanelControl implements Controller {

  private Nifty nifty;
  private Screen screen;
  private Element element;

  /**
   * Bind this controller to the given element.
   * @param niftyParam niftyParam
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Nifty niftyParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener) {
    nifty = niftyParam;
    element = newElement;
  }

  /**
   * Called when the screen is started.
   * @param newScreen newScreen
   */
  public void onStartScreen(final Screen newScreen) {
    screen = newScreen;
    setDifficulty("easy");
  }

  /**
   * This controller gets the focus.
   * @param getFocus get focus (true) or loose focus (false)
   */
  public void onFocus(final boolean getFocus) {
  }

  /**
   * input event.
 * @param inputEvent the NiftyInputEvent to process
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
  }

  public void removePanel() {
    nifty.removeElement(screen, element);
  }

  public void setDifficulty(final String mode) {
    element.findElementByName("easy").setStyle("unselected");
    element.findElementByName("medium").setStyle("unselected");
    element.findElementByName("hard").setStyle("unselected");
    element.findElementByName("expert").setStyle("unselected");

    if ("easy".equals(mode)) {
      element.findElementByName("easy").setStyle("selected");
    } else if ("medium".equals(mode)) {
      element.findElementByName("medium").setStyle("selected");
    } else if ("hard".equals(mode)) {
      element.findElementByName("hard").setStyle("selected");
    } else if ("expert".equals(mode)) {
      element.findElementByName("expert").setStyle("selected");
    }
  }
}
