package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.loader.xpp3.ClassHelper;

/**
 * RegisterEffectType.
 * @author void
 */
public class RegisterEffectType {

  /**
   * class name.
   */
  private String clazz;

  /**
   * Create new instance.
   * @param clazzParam clazz
   */
  public RegisterEffectType(final String clazzParam) {
    this.clazz = clazzParam;
  }

  /**
   * Get effect class.
   * @return effect class instance
   */
  public Class < ? > getEffectClass() {
    return ClassHelper.loadClass(clazz);
  }
}
