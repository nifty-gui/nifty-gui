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
   * @param newScreen the new nifty to set
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Nifty niftyParam,
      final Screen newScreen,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener) {
    nifty = niftyParam;
    screen = newScreen;
    element = newElement;
  }
  
  /**
   * Called when the screen is started.
   */
  public void onStartScreen() {
    
  }

  /**
   * This controller gets the focus.
   * @param getFocus get focus (true) or loose focus (false)
   */
  public void onFocus(boolean getFocus) {
    
  }

  /**
   * input event.
 * @param inputEvent the NiftyInputEvent to process
   */
  public void inputEvent(NiftyInputEvent inputEvent) {
    
  }
  
  public void easyClicked(final int x, final int y) {
    nifty.addControl(screen, screen.findElementByName("box-parent"), "multiplayerPanel", ""+Integer.valueOf(element.getId())+1);
  }
  
  public void mediumClicked(final int x, final int y) {
    nifty.removeElement(screen, element);
  }
  
}
