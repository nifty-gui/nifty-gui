package de.lessvoid.nifty.elements;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * A object and a method for the object.
 * @author void
 */
public class MethodInvoker {
  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(MethodInvoker.class.getName());

  /**
   * object.
   */
  private Object object;

  /**
   * method.
   */
  private Method method;

  /**
   * forwardInvoker.
   */
  private MethodInvoker forwardInvoker;

  /**
   * create null MethodInvoker.
   */
  public MethodInvoker() {
    this.object = null;
    this.method = null;
    this.forwardInvoker = null;
  }

  /**
   * @param objectParam object
   * @param methodParam method
   */
  public MethodInvoker(final Object objectParam, final Method methodParam) {
    this.object = objectParam;
    this.method = methodParam;
    this.forwardInvoker = null;
  }

  /**
   * get object.
   * @return object
   */
  public Object getObject() {
    return object;
  }

  /**
   * is null method?
   * @return true when null object
   */
  public boolean isNull() {
    return object == null || method == null;
  }

  /**
   * set forwardInvoker.
   * @param forwardInvokerParam forwardInvoker
   */
  public void setForward(final MethodInvoker forwardInvokerParam) {
    forwardInvoker = forwardInvokerParam;
  }

  /**
   * return count of actual method parameters.
   * @return count of parameters the method needs
   */
  private int getMethodParameterCount() {
    if (forwardInvoker != null) {
      return forwardInvoker.getMethodParameterCount();
    } else {
      Class < ? > [] parameterTypes = method.getParameterTypes();
      return parameterTypes.length;
    }
  }

  /**
   * invoke method with optional parameters.
   * @param invokeParametersParam parameter array for call
   */
  public void invoke(final Object ... invokeParametersParam) {
    if (forwardInvoker != null) {
      forwardInvoker.invoke(invokeParametersParam);
      return;
    }

    // now build invoke parameters
    Object [] invokeParameters = invokeParametersParam;
    if (invokeParameters.length == 0) {
      log.info("invoking method '" + method + "'");
    } else {
      if (!supportsParameters()) {
        log.info("invoking method '" + method + "' (note: given invokeParameters have been ignored)");
        invokeParameters = new Object[0];
      } else {
        StringBuffer paraString = new StringBuffer();
        paraString.append(invokeParameters[0].toString());
        for (int i = 1; i < invokeParameters.length; i++) {
          paraString.append(", ");
          paraString.append(invokeParameters[i].toString());
        }
        log.info("invoking method '" + method + "' with (" + paraString + ")");
      }
    }

    try {
      method.invoke(object, invokeParameters);
    } catch (Exception e) {
      log.warning("error: " + e.getMessage());
    }
  }

  /**
   * returns true when this method supports parameters and false when not.
   * @return true when parametercount != 0 (in this case parameters are supported)
   * and false if not
   */
  private boolean supportsParameters() {
    return getMethodParameterCount() != 0;
  }
}
