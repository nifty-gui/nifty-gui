package de.lessvoid.nifty.loader;

import java.util.Hashtable;
import java.util.logging.Logger;

import net.sourceforge.niftyGui.nifty.EffectType;
import net.sourceforge.niftyGui.nifty.PostType;
import net.sourceforge.niftyGui.nifty.RegisterEffectType;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.general.Effect;
import de.lessvoid.nifty.effects.general.EffectImpl;
import de.lessvoid.nifty.effects.general.StaticEffect;
import de.lessvoid.nifty.effects.hover.HoverEffect;
import de.lessvoid.nifty.effects.hover.HoverEffectImpl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.TimeProvider;

public class EffectConvert {

  /**
   * default value for post/pre effect state.
   */
  private static final boolean DEFAULT_EFFECT_POST = false;

  private static Logger log= Logger.getLogger(EffectConvert.class.getName());

  public static Effect convertEffect(
      Nifty nifty,
      EffectEventId effectEventId,
      Element element,
      Hashtable<String, RegisterEffectType> registerEffects,
      EffectType effectType) {

    String name= effectType.getName();
    RegisterEffectType registerEffectType= registerEffects.get( name );
    if( registerEffectType == null ) {
      log.info( "unable to convert effect: " + name + ". no effect with this name registered." );
      return null;
    }

    // get inherit
    boolean inherit = false;
    if (effectType.isSetInherit()) {
      inherit = effectType.getInherit();
    }

    // get post mode and default to post = true, when nothing is given
    boolean post = DEFAULT_EFFECT_POST;
    if (effectType.isSetPost()) {
      if (PostType.FALSE == effectType.getPost()) {
        post = false;
      } else if (PostType.TRUE == effectType.getPost()) {
        post = true;
      }
    }

    // get alternate key, this defaults to null
    String alternateKey = null;
    boolean alternateEnable = false;
    if (effectType.isSetAlternateEnable()) {
      alternateKey = effectType.getAlternateEnable();
      alternateEnable = true;
    } else if (effectType.isSetAlternateDisable()) {
      alternateKey = effectType.getAlternateDisable();
      alternateEnable = false;
    }

    // create the effect class
    if (effectEventId.equals(EffectEventId.onHover)) {
      HoverEffect hoverEffect = new HoverEffect(nifty, inherit, post, alternateKey, alternateEnable);
      hoverEffect.init(
          element,
          createHoverEffectImpl(registerEffectType.getClass1()),
          XmlBeansHelper.createProperties(effectType),
          new TimeProvider());
      return hoverEffect;
    } else {
      StaticEffect effect = new StaticEffect(nifty, inherit, post, alternateKey, alternateEnable);
      effect.init(
          element,
          createEffectImpl(registerEffectType.getClass1()),
          XmlBeansHelper.createProperties(effectType),
          new TimeProvider());
      return effect;
    }
  }

  /**
   * create EffectImpl.
   * @param className the className to instantiate
   * @return new EffectImpl instance
   */
  private static EffectImpl createEffectImpl(final String className) {
    try {
      Class < ? > cls = Loader.class.getClassLoader().loadClass(className);
      if (EffectImpl.class.isAssignableFrom(cls)) {
        return (EffectImpl) cls.newInstance();
      } else {
        log.warning("given effect class [" + className + "] does not implement [" + EffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      log.throwing(EffectConvert.class.getName(), "createEffectImpl", e);
    }
    return null;
  }

  /**
   * create HoverEffectImpl.
   * @param className the className to instantiate
   * @return new HoverEffectImpl instance
   */
  private static HoverEffectImpl createHoverEffectImpl(final String className) {
    try {
      Class < ? > cls = Loader.class.getClassLoader().loadClass(className);
      if (HoverEffectImpl.class.isAssignableFrom(cls)) {
        return (HoverEffectImpl) cls.newInstance();
      } else {
        log.warning("given effect class [" + className + "] does not implement [" + HoverEffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + className + "] could not be instanziated");
    }
    return null;
  }

}
