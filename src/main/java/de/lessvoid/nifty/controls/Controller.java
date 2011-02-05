package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * Controller.
 * @author void
 */
public interface Controller {

  /**
   * Bind this Controller to a certain element.
   * @param nifty nifty
   * @param element the Element
   * @param parameter parameters from the xml source to init the controller
   * @param listener the ControllerEventListener
   */
  void bind(
      Nifty nifty,
      Screen screen,
      Element element,
      Properties parameter,
      Attributes controlDefinitionAttributes);

  /**
   * Init the Controller. You can assume that bind() has been called for all other controls on the screen.
   * @param parameter
   * @param controlDefinitionAttributes
   */
  void init(Properties parameter, Attributes controlDefinitionAttributes);

  /**
   * Called when the screen is started.
   */
  void onStartScreen();

  /**
   * This controller gets the focus.
   * @param getFocus get focus (true) or loose focus (false)
   */
  void onFocus(boolean getFocus);

  /**
   * input event.
   * @param inputEvent the NiftyInputEvent to process
   * @return true, the event has been handled and false, the event has not been handled
   */
  boolean inputEvent(NiftyInputEvent inputEvent);
}
