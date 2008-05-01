package de.lessvoid.nifty.loader.xpp3.elements;

import java.lang.reflect.Method;

import de.lessvoid.nifty.elements.tools.MethodResolver;
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
   * @param controller screenController
   * @return method
   */
  public Method getMethod(final Object controller) {
    return getMethod(controller, value);
  }

  /**
   * Get Method.
   * @param controller ScreenController
   * @param methodName method name
   * @return resolved Method
   */
  private Method getMethod(final Object controller, final String methodName) {
    Method onClickMethod = MethodResolver.findMethod(controller.getClass(), methodName);
    if (onClickMethod == null) {
      return null;
    }
    return onClickMethod;
  }

}
