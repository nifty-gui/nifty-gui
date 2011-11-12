package de.lessvoid.xml.tools;

import java.util.logging.Logger;

/**
 * Helper to get Class from class name.
 * @author void
 */
public final class ClassHelper {

  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(ClassHelper.class.getName());

  /**
   * You can't initialize this class.
   */
  private ClassHelper() {
  }

  /**
   * Load Class with the given full qualified name.
   * @param className name of class to load
   * @return Class object or null
   */
  public static Class < ? > loadClass(final String className) {
    try {
      Class < ? > cls = ClassHelper.class.getClassLoader().loadClass(className);
      return cls;
    } catch (Exception e) {
      log.warning("class [" + className + "] could not be found (" + e.getMessage() + ")");
    }
    return null;
  }

  /**
   * dynamically load the given class, create and return a new instance.
   * @param className className
   * @param type type
   * @param <T> class
   * @return new ScreenController instance or null
   */
  public static < T > T getInstance(final String className, final Class < T > type) {
    try {
      Class < ? > cls = ClassHelper.class.getClassLoader().loadClass(className);
      if (type.isAssignableFrom(cls)) {
        return type.cast(cls.newInstance());
      } else {
        log.warning(
            "given class [" + className + "] does not implement [" + type.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + className + "] could not be instantiated (" + e.toString() + ")");
    }
    return null;
  }

  public static < T > T getInstance(final Class < T > clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      log.warning("class [" + clazz.getName() + "] could not be instantiated");
    }
    return null;
  }
}

