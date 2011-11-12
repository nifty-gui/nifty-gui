package de.lessvoid.nifty.controls;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * NiftyInputControl.
 * @author void
 */
public class NiftyInputControl {
  private Controller controller;
  private NiftyInputMapping inputMapper;

  private List < KeyInputHandler > preInputHandler = new ArrayList < KeyInputHandler >();
  private List < KeyInputHandler > postInputHandler = new ArrayList < KeyInputHandler >();

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
   * @param nifty nifty
   * @param inputEvent keyboard event
   * @return return true when the input event has been processed and false when it has not been handled
   */
  public boolean keyEvent(final Nifty nifty, final KeyboardInputEvent inputEvent, final String elementId) {
    NiftyInputEvent converted = inputMapper.convert(inputEvent);

    for (KeyInputHandler handler : preInputHandler) {
      if (handler.keyEvent(converted)) {
        return true;
      }
    }

    if (converted != null) {
        nifty.publishEvent(elementId, converted);
    }

    if (controller.inputEvent(converted)) {
      return true;
    }

    for (KeyInputHandler handler : postInputHandler) {
      if (handler.keyEvent(converted)) {
        return true;
      }
    }
    return false;
  }

  public void addInputHandler(final KeyInputHandler handler) {
    postInputHandler.add(handler);
  }

  public void addPreInputHandler(final KeyInputHandler handler) {
    preInputHandler.add(handler);
  }

  public void onStartScreen(final Nifty nifty, final Screen screen) {
    controller.onStartScreen();
  }

  public void onEndScreen(final Nifty nifty, final Screen screen, final String elementId) {
    nifty.unsubscribeAnnotations(controller);
    nifty.unsubscribeElement(screen, elementId);
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

  public < T extends NiftyControl > T getNiftyControl(final Class < T > requestedControlClass) {
    if (requestedControlClass.isInstance(controller)) {
      return requestedControlClass.cast(controller);
    }
    return null;
  }

  public void bindControl(final Nifty nifty, final Screen screen, final Element element, final Attributes attributes) {
    if (controller != null) {
      controller.bind(
          nifty,
          screen,
          element,
          attributes.createProperties(),
          attributes);
    }
  }

  public void initControl(final Attributes attributes) {
    if (controller != null) {
      controller.init(attributes.createProperties(), attributes);
    }
  }
}
