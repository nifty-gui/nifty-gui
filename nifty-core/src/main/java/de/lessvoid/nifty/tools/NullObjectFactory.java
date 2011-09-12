package de.lessvoid.nifty.tools;


import de.lessvoid.nifty.controls.NiftyControl;

public class NullObjectFactory {
  public static < T extends NiftyControl > T createNull(final Class < T > requestedControlClass) {
    try {
      String packageName = requestedControlClass.getPackage().getName();
      String className = requestedControlClass.getSimpleName();
      return requestedControlClass.cast(((Class<?>) Class.forName(packageName + ".nullobjects." + className + "Null")).newInstance());
    } catch (Exception e) {
      return null;
    }
  }
}
