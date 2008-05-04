package de.lessvoid.nifty.elements;

import java.util.Properties;

import de.lessvoid.nifty.screen.Screen;

/**
 * Controller.
 * @author void
 */
public interface ControlController extends Controller {

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

  /**
   * This controller gets the focus.
   */
  void onGetFocus();

  /**
   * This controller looses the focus.
   */
  void onLostFocus();

  /**
   * Forward a controller method call. This is used so that a controller for
   * a control (implementation of this interface) can actual forward methods
   * to a ScreenController. This way we can assure that a ControllController
   * can define own methods as well as forwards calls to a ScreenController
   * that are not handled here.
   * @param controllerMethod controller method
   */
  void forward(MethodInvoker controllerMethod);
}
