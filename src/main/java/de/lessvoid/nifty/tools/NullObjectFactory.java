package de.lessvoid.nifty.tools;

import java.util.logging.Logger;

import de.lessvoid.nifty.controls.NiftyControl;

public class NullObjectFactory {
  public static < T extends NiftyControl > T createNull(final String elementName, final Class < T > requestedControlClass, final Logger log) {
    log.warning("missing element/control with id [" + elementName + "] for requested control class [" + requestedControlClass.getName() + "]");

    try {
      String packageName = requestedControlClass.getPackage().getName();
      String className = requestedControlClass.getSimpleName();
      return requestedControlClass.cast(((Class<?>) Class.forName(packageName + ".nullobjects." + className + "Null")).newInstance());
    } catch (Exception e) {
      return null;
    }
  }
}
