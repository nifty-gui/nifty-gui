package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.input.NiftyInputMapping;

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
   * handle key event.
   * @param eventKey eventKey
   * @param eventCharacter eventCharacter
   * @param keyDown keyDown
   */
  public void keyEvent(final int eventKey, final char eventCharacter, final boolean keyDown) {
    controller.inputEvent(inputMapper.convert(eventKey, eventCharacter, keyDown));
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
