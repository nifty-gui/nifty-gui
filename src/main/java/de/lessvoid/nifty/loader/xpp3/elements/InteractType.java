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
  private OnClickType onClick;
  private OnClickType onRelease;
  private OnClickType onMouseOver;
  private OnClickType onClickRepeat;
  private OnClickType onClickMouseMove;

  private String onClickAlternateKey;

  /**
   * copy constructor.
   * @param source source
   */
  public InteractType(final InteractType source) {
    onClick = new OnClickType(source.onClick);
    onRelease = new OnClickType(source.onRelease);
    onClickRepeat = new OnClickType(source.onClickRepeat);
    onClickMouseMove = new OnClickType(source.onClickMouseMove);
    onMouseOver = new OnClickType(source.onMouseOver);
    onClickAlternateKey = source.onClickAlternateKey;
  }

  public InteractType() {
  }

  public void setOnClick(final OnClickType onClickParam) {
    this.onClick = onClickParam;
  }

  public void setOnRelease(final OnClickType onReleaseParam) {
    this.onRelease = onReleaseParam;
  }

  /**
   * Set onMouseOver.
   * @param onMouseOverParam onClick
   */
  public void setOnMouseOver(final OnClickType onMouseOverParam) {
    this.onMouseOver = onMouseOverParam;
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
   * initialize with input control.
   * @param element element
   * @param controller input controllers
   */
  public void initWithControl(
      final Element element,
      final Object ... controller) {
    initElement(element, controller);
  }

  /**
   * initialize with screen controller.
   * @param element element
   * @param screenController screen controller
   */
  public void initWithScreenController(
      final Element element,
      final ScreenController screenController) {
    initElement(element, screenController);
  }

  /**
   * Initialize element.
   * @param element element
   * @param controlController controlController
   */
  private void initElement(final Element element, final Object ... controlController) {
    if (onClick != null) {
      MethodInvoker onClickMethod = onClick.getMethod(controlController);
      element.setOnClickMethod(onClickMethod, false);
      element.setVisibleToMouseEvents(true);
    }
    if (onRelease != null) {
      MethodInvoker onReleaseMethod = onRelease.getMethod(controlController);
      element.setOnReleaseMethod(onReleaseMethod);
      element.setVisibleToMouseEvents(true);
    }
    if (onMouseOver != null) {
      MethodInvoker onClickMethod = onMouseOver.getMethod(controlController);
      element.setOnMouseOverMethod(onClickMethod);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickRepeat != null) {
      MethodInvoker onClickRepeatMethod = onClickRepeat.getMethod(controlController);
      element.setOnClickMethod(onClickRepeatMethod, true);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickMouseMove != null) {
      MethodInvoker onClickMouseMoveMethod = onClickMouseMove.getMethod(controlController);
      element.setOnClickMouseMoveMethod(onClickMouseMoveMethod);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickAlternateKey != null) {
      element.setOnClickAlternateKey(onClickAlternateKey);
    }
  }
}
