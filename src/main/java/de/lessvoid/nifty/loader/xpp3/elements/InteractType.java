package de.lessvoid.nifty.loader.xpp3.elements;

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
   * init element.
   * @param element element
   * @param controller ScreenController
   */
  public void initElement(
      final Element element,
      final Object controller) {

    if (onClick != null) {
      element.setOnClickMethod(onClick.getMethod(controller), controller, false);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickRepeat != null) {
      element.setOnClickMethod(onClickRepeat.getMethod(controller), controller, true);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickMouseMove != null) {
      element.setOnClickMouseMoveMethod(onClickMouseMove.getMethod(controller), controller);
      element.setVisibleToMouseEvents(true);
    }
    if (onClickAlternateKey != null) {
      element.setOnClickAlternateKey(onClickAlternateKey);
    }
  }
}
