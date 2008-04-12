package de.lessvoid.nifty.elements;

import java.util.Properties;

import de.lessvoid.nifty.screen.Screen;

/**
 * Controller.
 * @author void
 */
public interface Controller {

  /**
   * Bind this Controller to a certain element.
   * @param screen the Screen
   * @param element the Element
   * @param parameter parameters from the xml source to init the controller
   * @param listener the ControllerEventListener
   */
  void bind(Screen screen, Element element, Properties parameter, ControllerEventListener listener);

  /**
   * Called when the screen is started.
   */
  void onStartScreen();

  /**
   * key event.
   * @param eventKey event key
   * @param keyEvent key event
   * @param keyDown key down
   */
  void keyEvent(int eventKey, char keyEvent, boolean keyDown);

  void onGetFocus();

  void onLostFocus();
}
