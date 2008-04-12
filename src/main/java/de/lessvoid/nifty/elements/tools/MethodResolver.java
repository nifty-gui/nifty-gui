package de.lessvoid.nifty.elements.tools;

import java.lang.reflect.Method;

/**
 * MethodResolver helper class.
 * @author void
 */
public final class MethodResolver {

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
  public static Method findMethod(
      final Class < ? > c,
      final String methodName) {
    if (c == null) {
      return null;
    }

    String methodNameOnly = methodName.substring(0, methodName.indexOf('('));
    Method[] ms = c.getDeclaredMethods();
    for (Method m : ms) {
      if (methodNameOnly.equalsIgnoreCase(m.getName())) {
        return m;
      }
    }

    return findMethod(c.getSuperclass(), methodName);
  }
}
