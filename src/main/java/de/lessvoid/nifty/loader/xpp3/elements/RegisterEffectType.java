package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.loader.xpp3.ClassHelper;

/**
 * RegisterEffectType.
 * @author void
 */
public class RegisterEffectType {
  /**
   * name.
   */
  private String name;

  /**
   * class name.
   */
  private String clazz;

  /**
   * Create new instance.
   * @param nameParam name
   * @param clazzParam clazz
   */
  public RegisterEffectType(final String nameParam, final String clazzParam) {
    this.name = nameParam;
    this.clazz = clazzParam;
  }

  /**
   * Get effect class.
   * @return effect class instance
   */
  public Class < ? > getEffectClass() {
    Class < ? > cl = ClassHelper.loadClass(clazz);
    return cl;
//      log.info("register effect [" + name + "]->[" + clazz + "]");
//      registerEffects.put(effectName, cl);
  }
}
