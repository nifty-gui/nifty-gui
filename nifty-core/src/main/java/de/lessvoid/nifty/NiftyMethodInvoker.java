package de.lessvoid.nifty;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import de.lessvoid.xml.tools.MethodResolver;

/**
 * A object and a method for the object.
 * @author void
 */
public class NiftyMethodInvoker implements NiftyDelayedMethodInvoke {
  private static Logger log = Logger.getLogger(NiftyMethodInvoker.class.getName());

  private Object[] target;
  private String methodWithName;
  private Nifty nifty;

  /**
   * create null MethodInvoker.
   */
  public NiftyMethodInvoker(final Nifty nifty) {
    this.nifty = nifty;
    this.methodWithName = null;
    this.target = null;
  }

  /**
   * Create new MethodInvoker.
   * @param methodParam method
   * @param targetParam target
   */
  public NiftyMethodInvoker(final Nifty nifty, final String methodParam, final Object ... targetParam) {
    this.nifty = nifty;
    this.methodWithName = methodParam;
    if (targetParam == null || targetParam.length == 0) {
      this.target = null;
    } else {
      this.target = new Object[targetParam.length];
      System.arraycopy(targetParam, 0, target, 0, targetParam.length);

      log.fine("target objects for [" + methodWithName + "]");
      for (Object o : target) {
        log.fine(o.toString());
      }
    }
  }

  /**
   * invoke method with optional parameters.
   * @param invokeParametersParam parameter array for call
   * @return true, method has been scheduled and false, method couldn't be called
   */
  public boolean invoke(final Object ... invokeParametersParam) {
    // nothing to do?
    if (target == null || target.length == 0 || methodWithName == null) {
      return false;
    }
    nifty.delayedMethodInvoke(this, invokeParametersParam);

    // this true means we are super optimistic that the actual call will be successful
    // and therefore the event will be processed. returning true in here does ultimately mean
    // that the caller will stop the processing of the event (when the source for the invoke
    // was an event that is).
    return true;
  }

  public void performInvoke(final Object ... invokeParametersParam) {
    // process all methods (first one wins)
    for (Object object : target) {
      if (object != null) {
        Method method = MethodResolver.findMethod(object.getClass(), methodWithName);
        if (method != null) {
          // we've found a method with the given name. now we need to match the parameters.
          //
          // 1) if the method we want to call (the string from the xml that is!) has parameters
          //    encoded we ignore the actual invokeParametersParam we've been called with and
          //    call the method as is.
          //
          // 2) if the method we want to call has no parameters there are two possibilities:
          //    2a) invokeParametersParam are given, in this case we'll try to forward them to the method
          //        if this is not possible we fall back to 2b)
          //    2b) just call the method without any parameters
          Object methodResult = null;
          Object[] invokeParameters = MethodResolver.extractParameters(methodWithName);
          if (invokeParameters.length > 0) {
            // does the method supports the parameters?
            // TODO: not only check for the count but check the type too
            if (getMethodParameterCount(method) == invokeParameters.length) {
              log.fine("invoking method '" + methodWithName + "' with (" + debugParaString(invokeParameters) + ")");
              methodResult = callMethod(object, method, invokeParameters);
            } else {
              log.fine("invoking method '" + methodWithName + "' (note: given invokeParameters have been ignored)");
              methodResult = callMethod(object, method, new Object[0]);
            }
          } else {
            // no invokeParameters encoded. this means we can call the method as is or with the invokeParametersParam
            if (invokeParametersParam.length > 0) {
              if (getMethodParameterCount(method) == invokeParametersParam.length) {
                log.fine("invoking method '" + methodWithName + "' with the actual parameters (" + debugParaString(invokeParametersParam) + ")");
                methodResult = callMethod(object, method, invokeParametersParam);
              } else {
                log.fine("invoking method '" + methodWithName + "' without parameters (invokeParametersParam mismatch)");
                methodResult = callMethod(object, method, null);
              }
            } else {
              log.fine("invoking method '" + methodWithName + "' without parameters");
              methodResult = callMethod(object, method, null);
            }
          }
          if (methodResult != null && (methodResult.getClass().equals(Boolean.class))) {
            if ((Boolean) methodResult) {
              log.fine("method invoke for '" + methodWithName + "' returns true. by definition this means we're not calling any other targets for this method.");
              return;
            }
            
          }
        } else {
          log.fine("method [" + methodWithName + "] not found at object class [" + object.getClass() + "]");
        }
      } else {
        log.warning("target object is null");
      }
    }
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
      log.fine("method: " + method + "on targetObject: " + targetObject + ", parameters: " + invokeParameters);
      if (method != null) {
        log.fine(method.getName());
      }
      if (invokeParameters != null) {
        for (Object o : invokeParameters) {
          log.fine("parameter: " + o);
        }
      }
      return method.invoke(targetObject, invokeParameters);
    } catch (RuntimeException e) {
      log.warning("RuntimeException: " + e.toString());
      logException(e);
      return null;
    } catch (Exception e) {
      log.warning("Exception: " + e.toString());
      logException(e);
      return null;
    }
  }

  private void logException(final Throwable e) {
    StackTraceElement[] elements = e.getStackTrace();
    if (elements == null) {
      log.warning("stacktrace is null");
    } else {
      for (StackTraceElement ee : elements) {
        log.warning(ee.getClassName() + " " + ee.getMethodName() + " (" + ee.getFileName() + ":" + ee.getLineNumber() + ")");
      }
    }
    if (e.getCause() != null) {
      log.warning("Root Cause: " + e.getCause().toString());
      logException(e.getCause());
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
    paraStringBuffer.append(invokeParameters[0]);
    for (int i = 1; i < invokeParameters.length; i++) {
      paraStringBuffer.append(", ");
      paraStringBuffer.append(invokeParameters[i]);
    }
    return paraStringBuffer.toString();
  }
}
