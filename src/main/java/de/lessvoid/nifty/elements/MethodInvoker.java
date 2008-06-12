package de.lessvoid.nifty.elements;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import de.lessvoid.nifty.elements.tools.MethodResolver;

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
  private Object[] target;

  /**
   * method.
   */
  private String methodWithName;

  /**
   * create null MethodInvoker.
   */
  public MethodInvoker() {
    this.methodWithName = null;
    this.target = null;
  }

  /**
   * Create new MethodInvoker.
   * @param methodParam method
   * @param targetParam target
   */
  public MethodInvoker(final String methodParam, final Object ... targetParam) {
    this.methodWithName = methodParam;
    this.target = targetParam;
  }

  /**
   * Set first.
   * @param object object
   */
  public void setFirst(final Object object) {
    if (methodWithName == null) {
      return;
    }
    if (target == null) {
      target = new Object[1];
    }
    this.target[0] = object;
  }

  /**
   * invoke method with optional parameters.
   * @param invokeParametersParam parameter array for call
   */
  public void invoke(final Object ... invokeParametersParam) {
    // nothing to do?
    if (target == null || target.length == 0 || methodWithName == null) {
      return;
    }

    // process all methods (first one wins)
    for (Object object : target) {
      if (object != null) {
        Method method = MethodResolver.findMethod(object.getClass(), methodWithName);
        if (method != null) {
          // there really are two different modes:
          // a) there are invokeParametersParam given
          //    in this case we will call the method with the parameters given
          // b) there are no invokeParametersParam given
          //    in this case we well look for predefined parameters to call the method with
          Object[] invokeParameters = new Object[0];
          if (invokeParametersParam.length > 0) {
            invokeParameters = invokeParametersParam;
          } else {
            // now build invoke parameters
            invokeParameters = MethodResolver.extractParameters(methodWithName);
            if (invokeParameters.length == 0) {
              log.info("invoking method '" + methodWithName + "' without parameters");
            } else {
              if (!supportsParameters(method)) {
                log.info("invoking method '" + methodWithName + "' (note: given invokeParameters have been ignored)");
                invokeParameters = new String[0];
              } else {
                log.info("invoking method '" + methodWithName + "' with (" + debugParaString(invokeParameters) + ")");
              }
            }
          }

          try {
            method.invoke(object, invokeParameters);
          } catch (Exception e) {
            log.warning("error: " + e.getMessage());
          }

          return;
        }
      }
    }
  }

  /**
   * helper method to convert the given parameter object array into a string for debugging.
   * @param invokeParameters parameter array
   * @return string of parameter array in debug output friendly way
   */
  private String debugParaString(final Object[] invokeParameters) {
    StringBuffer paraStringBuffer = new StringBuffer();
    paraStringBuffer.append(invokeParameters[0].toString());
    for (int i = 1; i < invokeParameters.length; i++) {
      paraStringBuffer.append(", ");
      paraStringBuffer.append(invokeParameters[i].toString());
    }
    return paraStringBuffer.toString();
  }

  /**
   * returns true when this method supports parameters and false when not.
   * @param method method to check
   * @return true when parameterCount != 0 (in this case parameters are supported)
   * and false if not
   */
  private boolean supportsParameters(final Method method) {
    return getMethodParameterCount(method) != 0;
  }

  /**
   * return count of actual method parameters.
   * @param method method
   * @return count of parameters the method needs
   */
  private int getMethodParameterCount(final Method method) {
    Class < ? > [] parameterTypes = method.getParameterTypes();
    return parameterTypes.length;
  }

}
