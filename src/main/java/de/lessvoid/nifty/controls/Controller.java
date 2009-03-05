package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControllerEventListener;
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
      Element element,
      Properties parameter,
      ControllerEventListener listener,
      Attributes controlDefinitionAttributes);

  /**
   * Called when the screen is started.
   * @param screen screen
   */
  void onStartScreen(final Screen screen);

  /**
   * This controller gets the focus.
   * @param getFocus get focus (true) or loose focus (false)
   */
  void onFocus(boolean getFocus);

  /**
   * input event.
   * @param inputEvent the NiftyInputEvent to process
   */
  void inputEvent(NiftyInputEvent inputEvent);
}
