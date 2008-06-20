package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * NiftyInputControl.
 * @author void
 */
public class NiftyInputControl {
  /**
   * controller.
   */
  private Controller controller;

  /**
   * input mapper this control uses.
   */
  private NiftyInputMapping inputMapper;

  /**
   * @param controllerParam controller
   * @param inputMapperParam input mapper
   */
  public NiftyInputControl(
      final Controller controllerParam,
      final NiftyInputMapping inputMapperParam) {
    this.controller = controllerParam;
    this.inputMapper = inputMapperParam;
  }

  /**
   * keyboard event.
   * @param inputEvent keyboard event
   */
  public void keyEvent(final KeyboardInputEvent inputEvent) {
    controller.inputEvent(inputMapper.convert(inputEvent));
  }

  /**
   * forward the onStartScreen event to the controller.
   */
  public void onStartScreen() {
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
}
