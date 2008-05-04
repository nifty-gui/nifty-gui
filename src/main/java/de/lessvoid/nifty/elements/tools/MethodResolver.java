package de.lessvoid.nifty.elements.tools;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import de.lessvoid.nifty.loader.xpp3.elements.OnClickType;

/**
 * MethodResolver helper class.
 * @author void
 */
public final class MethodResolver {
  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(OnClickType.class.getName());

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
      log.info("trying to resolve method [" + methodName + "] failed");
      return null;
    }

    String methodNameOnly = methodName.substring(0, methodName.indexOf('('));
    Method[] ms = c.getDeclaredMethods();
    for (Method m : ms) {
      if (methodNameOnly.equalsIgnoreCase(m.getName())) {
        log.info("trying to resolve method [" + methodName + "] on [" + c.toString() + "] = success");
        return m;
      }
    }

    log.info("trying to resolve method [" + methodName + "] on [" + c.toString() + "] = failed, trying base class");
    return findMethod(c.getSuperclass(), methodName);
  }
}
