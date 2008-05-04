package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.elements.ControlController;
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
   * Initialize element.
   * @param element element
   * @param controlController controlController
   * @param screenController screenController
   */
  public void initElement(
      final Element element,
      final ControlController controlController,
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
