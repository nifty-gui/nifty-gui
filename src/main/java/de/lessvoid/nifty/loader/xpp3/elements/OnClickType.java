package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.MethodInvoker;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * OnClickType.
 * @author void
 */
public class OnClickType {
  /**
   * value.
   */
  private String value;

  /**
   * copy constructor.
   * @param source source
   */
  public OnClickType(final OnClickType source) {
    this.value = source.value;
  }

  /**
   * OnClickType.
   * @param valueParam value
   */
  public OnClickType(final String valueParam) {
    this.value = valueParam;
  }

  /**
   * check if the value is valid.
   * @return true when valid, false when not valid
   */
  public boolean isValid() {
    if (value == null) {
      return false;
    }

    return value.matches("\\w+\\((|\\w+(,\\s*\\w+)*)\\)");
  }

  /**
   * get method.
   * @param screenController screenController
   * @param controlController controlController
   * @return method
   */
  public MethodInvoker getMethod(final Object ... controlController) {
    return new MethodInvoker(value, controlController);
  }
}
