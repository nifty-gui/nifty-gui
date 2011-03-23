package de.lessvoid.xml.tools;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * MethodResolver helper class.
 * @author void
 */
public class MethodResolver {
  private static Logger log = Logger.getLogger(MethodResolver.class.getName());

  /**
   * you can't instantiate this class it's a helper class.
   */
  private MethodResolver() {
  }

  /**
   * find a method per name in the given class.
   * @param c the class to look for
   * @param methodName the methodName
   * @return the Method instance
   */
  public static Method findMethod(final Class < ? > c, final String methodName) {
    if (c == null) {
      return null;
    }
    String methodNameOnly = extractMethodName(methodName);
    if (methodNameOnly == null) {
      log.warning("Could not extract method from [" + methodName + "]");
      return null;
    }
    Method[] ms = c.getMethods();
    for (Method m : ms) {
      if (methodNameOnly.equalsIgnoreCase(m.getName())) {
        return m;
      }
    }
    return findMethod(c.getSuperclass(), methodName);
  }

  public static Method findMethodWithArgs(final Class<?> c, final String methodName, final Class<?> ... parameters) {
    if (c == null) {
      return null;
    }
    String methodNameOnly = extractMethodName(methodName);
    if (methodNameOnly == null) {
      log.warning("Could not extract method from [" + methodName + "]");
      return null;
    }
    Method[] ms = c.getMethods();
    for (Method m : ms) {
      if (methodNameOnly.equalsIgnoreCase(m.getName())) {
        if (Arrays.equals(m.getParameterTypes(), parameters)) {
          return m;
        }
      }
    }
    return null;
  }

  /**
   * extract array of strings encoding in the given method string.
   * @param methodName method
   * @return array of strings with actual parameters or empty array
   */
  public static String[] extractParameters(final String methodName) {
    String parameterString = extractArgs(methodName);
    if (parameterString == null || parameterString.length() == 0) {
      return new String[0];
    }

    String[] result = parameterString.split(",");
    for (int i = 0; i < result.length; i++) {
      result[i] = result[i].trim();
    }
    return result;
  }

  /**
   * extract the part within ().
   * @param methodName complete methodname with argument list in ()
   * @return the part within ()
   */
  public static String extractArgs(final String methodName) {
    int startIdx = methodName.indexOf("(");
    int endIdx = methodName.lastIndexOf(")");
    if (startIdx == -1 || endIdx == -1) {
      return "";
    }
    return methodName.substring(startIdx + 1, endIdx);
  }

  private static String extractMethodName(final String methodName) {
    if (!methodName.contains("(")) {
      return null;
    }
    return methodName.substring(0, methodName.indexOf('('));
  }
}
