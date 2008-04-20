package de.lessvoid.nifty.loader.xpp3;

import java.util.logging.Logger;

import de.lessvoid.nifty.screen.ScreenController;

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
   * Dynamically load and create the given class.
   * @param controllerClass class of controller
   * @return new ScreenController instance or null on errors
   */
  public static ScreenController getScreenController(final String controllerClass) {
    try {
      Class < ? > cls = ClassHelper.class.getClassLoader().loadClass(controllerClass);
      if (ScreenController.class.isAssignableFrom(cls)) {
        return (ScreenController) cls.newInstance();
      } else {
        log.warning(
            "given class [" + controllerClass + "] does not implement [" + ScreenController.class.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + controllerClass + "] could not be instanziated");
    }
    return null;
  }

}
