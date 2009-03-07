package de.lessvoid.nifty.controls;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;

/**
 * NiftyInputControl.
 * @author void
 */
public class NiftyInputControl {
  private Controller controller;
  private NiftyInputMapping inputMapper;

  private List < KeyInputHandler > additionalInputHandler = new ArrayList < KeyInputHandler >();

  /**
   * @param controllerParam controller
   * @param inputMapperParam input mapper
   */
  public NiftyInputControl(final Controller controllerParam, final NiftyInputMapping inputMapperParam) {
    this.controller = controllerParam;
    this.inputMapper = inputMapperParam;
  }

  /**
   * keyboard event.
   * @param inputEvent keyboard event
   */
  public void keyEvent(final KeyboardInputEvent inputEvent) {
    NiftyInputEvent converted = inputMapper.convert(inputEvent);
    controller.inputEvent(converted);

    for (KeyInputHandler handler : additionalInputHandler) {
      if (handler.keyEvent(converted)) {
        break;
      }
    }
  }

  /**
   * add an additional input handler.
   * @param handler KeyInputHandler
   */
  public void addInputHandler(final KeyInputHandler handler) {
    additionalInputHandler.add(handler);
  }

  /**
   * forward the onStartScreen event to the controller.
   * @param screen screen
   */
  public void onStartScreen(final Screen screen) {
    controller.onStartScreen();
  }

  /**
   * forward the onForward method to the controller.
   * @param getFocus get focus
   */
  public void onFocus(final boolean getFocus) {
    controller.onFocus(getFocus);
  }

  /**
   * get controller.
   * @return controller
   */
  public Controller getController() {
    return controller;
  }

  /**
   * Get control when it matches the given class.
   * @param <T> type of class
   * @param requestedControlClass class that is requested
   * @return the instance or null
   */
  public < T extends Controller > T getControl(final Class < T > requestedControlClass) {
    if (requestedControlClass.isInstance(controller)) {
      return requestedControlClass.cast(controller);
    }
    return null;
  }
}
