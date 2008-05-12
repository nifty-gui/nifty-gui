package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.MethodInvoker;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * InteractType.
 * @author void
 */
public class InteractType {
  /**
   * onClick.
   */
  private OnClickType onClick;

  /**
   * onClickRepeat.
   */
  private OnClickType onClickRepeat;

  /**
   * onClickMouseMove.
   */
  private OnClickType onClickMouseMove;

  /**
   * onClickAlternateKey.
   */
  private String onClickAlternateKey;

  /**
   * Set onClick.
   * @param onClickParam onClick
   */
  public void setOnClick(final OnClickType onClickParam) {
    this.onClick = onClickParam;
  }

  /**
   * Set onClickRepeat.
   * @param onClickRepeatParam onClickRepeat
   */
  public void setOnClickRepeat(final OnClickType onClickRepeatParam) {
    this.onClickRepeat = onClickRepeatParam;
  }

  /**
   * Set onClickMouseMove.
   * @param onClickMouseMoveParam onClickMouseMove
   */
  public void setOnClickMouseMove(final OnClickType onClickMouseMoveParam) {
    this.onClickMouseMove = onClickMouseMoveParam;
  }

  /**
   * Set onClickAlternateKey.
   * @param onClickAlternateKeyParam onClickAlternateKey
   */
  public void setOnClickAlternateKey(final String onClickAlternateKeyParam) {
    this.onClickAlternateKey = onClickAlternateKeyParam;
  }

  /**
   * init with input control.
   * @param element element
   * @param control input control
   * @param screenController screen controller
   */
  public void initWithControl(
      final Element element,
      final NiftyInputControl control,
      final ScreenController screenController) {
    element.attachInputControl(control);
    initElement(element, control.getController(), screenController);
  }

  /**
   * initialize with screen controller.
   * @param element element
   * @param screenController screen controller
   */
  public void initWithScreenController(
      final Element element,
      final ScreenController screenController) {
    initElement(element, null, screenController);
  }

  /**
   * Initialize element.
   * @param element element
   * @param controlController controlController
   * @param screenController screenController
   */
  private void initElement(
      final Element element,
      final Controller controlController,
      final ScreenController screenController) {

    if (onClick != null) {
      MethodInvoker onClickMethod = onClick.getMethod(controlController, screenController);
      element.setOnClickMethod(onClickMethod, false);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickRepeat != null) {
      MethodInvoker onClickRepeatMethod = onClickRepeat.getMethod(controlController, screenController);
      element.setOnClickMethod(onClickRepeatMethod, true);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickMouseMove != null) {
      MethodInvoker onClickMouseMoveMethod = onClickMouseMove.getMethod(controlController, screenController);
      element.setOnClickMouseMoveMethod(onClickMouseMoveMethod);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickAlternateKey != null) {
      element.setOnClickAlternateKey(onClickAlternateKey);
    }
  }
}
