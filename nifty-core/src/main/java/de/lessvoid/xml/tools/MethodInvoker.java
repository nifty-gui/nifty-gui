package de.lessvoid.xml.tools;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A object and a method for the object.
 * @author void
 */
public class MethodInvoker {
  private static Logger log = Logger.getLogger(MethodInvoker.class.getName());

  private Object[] target;
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
    if (targetParam == null || targetParam.length == 0) {
      this.target = null;
    } else {
      this.target = new Object[targetParam.length];
      int idx = this.target.length - 1;
      for (Object o : targetParam) {
        this.target[idx] = o;
        idx--;
      }
    }
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
      target[0] = object;
    } else {
      // scan current target array for the given object (is this already attached to the param object?)
      for (Object o : target) {
        if (o == object) {
          // already in -> done
          return;
        }
      }

      // alright object is not already in list -> add as last element to the array
      Object[] copy = new Object[target.length + 1];
      System.arraycopy(target, 0, copy, 0, target.length);
      copy[copy.length - 1] = object;
      target = copy;
    }
  }

  /**
   * invoke method with optional parameters.
   * @param invokeParametersParam parameter array for call
   * @return result of call
   */
  public Object invoke(final Object ... invokeParametersParam) {
    // nothing to do?
    if (target == null || target.length == 0 || methodWithName == null) {
      return null;
    }

    // process all methods (first one wins)
    for (Object object : target) {
      if (object != null) {
        Method method = MethodResolver.findMethod(object.getClass(), methodWithName);
        if (method != null) {
          // we've found a method with the given name. now we need to match the parameters.
          //
          // 1) if the target method has parameters encoded we ignore the invokeParametersParam we've been
          //    called with and call the method as is.
          // 2) if the target method has no parameters there are two possibilities:
          //    2a) invokeParametersParam are given, in this case we'll try to forward them to the method
          //        if this is not possible we fall back to 2b)
          //    2b) just call the method without any parameters
          Object[] invokeParameters = MethodResolver.extractParameters(methodWithName);
          if (invokeParameters.length > 0) {
            // does the method supports the parameters?
            // TODO: not only check for the count but check the type too
            if (getMethodParameterCount(method) == invokeParameters.length) {
              if (log.isLoggable(Level.FINE)) {
                log.fine("invoking method '" + methodWithName + "' with (" + debugParaString(invokeParameters) + ")");
              }
              return callMethod(object, method, invokeParameters);
            } else {
              if (log.isLoggable(Level.FINE)) {
                log.fine("invoking method '" + methodWithName + "' (note: given invokeParameters have been ignored)");
              }
              return callMethod(object, method, new Object[0]);
            }
          } else {
            // no invokeParameters encoded. this means we can call the method as is or with the invokeParametersParam
            if (invokeParametersParam.length > 0) {
              if (getMethodParameterCount(method) == invokeParametersParam.length) {
                if (log.isLoggable(Level.FINE)) {
                  log.fine("invoking method '" + methodWithName + "' with the actual parameters ("
                    + debugParaString(invokeParametersParam) + ")");
                }
                return callMethod(object, method, invokeParametersParam);
              } else {
                if (log.isLoggable(Level.FINE)) {
                  log.fine("invoking method '" + methodWithName
                    + "' without parameters (invokeParametersParam mismatch)");
                }
                return callMethod(object, method, null);
              }
            } else {
              if (log.isLoggable(Level.FINE)) {
                log.fine("invoking method '" + methodWithName + "' without parameters");
              }
              return callMethod(object, method, null);
            }
          }
        }
      }
    }
    log.warning("invoke for method [" + methodWithName + "] failed");
    return null;
  }

  /**
   * Invoke the given method on the given object.
   * @param targetObject target object to invoke method on
   * @param method method to invoke
   * @param invokeParameters parameters to use
   * @return result
   */
  private Object callMethod(final Object targetObject, final Method method, final Object[] invokeParameters) {
    try {
      if (log.isLoggable(Level.FINE)) {
        log.fine("method: " + method + "on targetObject: " + targetObject + ", parameters: " + invokeParameters);
        if (method != null) {
          log.fine(method.getName());
        }
        if (invokeParameters != null) {
          for (Object o : invokeParameters) {
            log.fine("parameter: " + o);
          }
        }
      }
      return method.invoke(targetObject, invokeParameters);
    } catch (Exception e) {
      log.warning("error: " + e.getMessage());
      StackTraceElement[] elements = e.getStackTrace();
      if (elements == null) {
        log.warning("stacktrace is null");
      } else {
        for (StackTraceElement ee : elements) {
          log.warning(
              ee.getClassName()
              + ":" + ee.getFileName()
              + ":" + ee.getMethodName()
              + ":" + ee.getLineNumber());
        }
      }
      return null;
    }
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
}
