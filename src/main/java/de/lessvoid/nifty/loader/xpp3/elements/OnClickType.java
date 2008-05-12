package de.lessvoid.nifty.loader.xpp3.elements;

import java.lang.reflect.Method;

import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.MethodInvoker;
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
   * @param controlController controlController
   * @param screenController screenController
   * @return method
   */
  public MethodInvoker getMethod(
      final Controller controlController,
      final ScreenController screenController) {
    if (controlController != null) {
      return resolveControlControllerMethod(controlController, screenController);
    }

    // screen controller method
    return resolveScreenControllerMethod(screenController);
  }

  /**
   * resolve control controller method.
   * @param controlController controlController
   * @param screenController screenController
   * @return MethodInvoker (might be a forward on the controlController)
   */
  private MethodInvoker resolveControlControllerMethod(
      final Controller controlController,
      final ScreenController screenController) {
    // alright, first check for method directly on control controller
    MethodInvoker controlMethod = getMethod(value, controlController);
    if (!controlMethod.isNull()) {
      return controlMethod;
    }

    // not directly found, wrap it as a call to the forward method on the controlController
    controlMethod = getMethod("forward()", controlController);
    controlMethod.setForward(resolveScreenControllerMethod(screenController));
    return controlMethod;
  }

  /**
   * resolve screen controller method.
   * @param screenController controller method
   * @return resolved Method
   */
  private MethodInvoker resolveScreenControllerMethod(final ScreenController screenController) {
    return getMethod(value, screenController);
  }

  /**
   * Get Method.
   * @param object object
   * @param methodName method name
   * @return resolved Method
   */
  private MethodInvoker getMethod(final String methodName, final Object object) {
    Method method = MethodResolver.findMethod(object.getClass(), methodName);
    if (method != null) {
      return new MethodInvoker(object, method);
    }
    return new MethodInvoker(null, null);
  }
}
