package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.general.EffectImpl;
import de.lessvoid.nifty.effects.general.StaticEffect;
import de.lessvoid.nifty.effects.hover.HoverEffect;
import de.lessvoid.nifty.effects.hover.HoverEffectImpl;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * EffectType.
 * @author void
 */
public class EffectType {
  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(EffectType.class.getName());

  /**
   * name.
   */
  private String name;

  /**
   * post.
   * @optional
   */
  private boolean post;

  /**
   * alternateEnable.
   */
  private String alternateKey;

  /**
   * alternateEnable.
   */
  private boolean alternateEnable;

  /**
   * inherit.
   */
  private boolean inherit;

  /**
   * any.
   */
  private Attributes any;

  /**
   * create it.
   * @param attributes attributes
   */
  public EffectType(final Attributes attributes) {
    any = attributes;
  }

  /**
   * setName.
   * @param nameParam name
   */
  public void setName(final String nameParam) {
    this.name = nameParam;
  }

  /**
   * setAlternateKey.
   * @param alternateKeyParam alternateKey
   */
  public void setAlternateKey(final String alternateKeyParam) {
    this.alternateKey = alternateKeyParam;
  }

  /**
   * setAlternateDisable.
   * @param alternateEnableParam alternateDisable
   */
  public void setAlternateEnable(final boolean alternateEnableParam) {
    this.alternateEnable = alternateEnableParam;
  }

  /**
   * setInherit.
   * @param inheritParam inherit
   */
  public void setInherit(final boolean inheritParam) {
    this.inherit = inheritParam;
  }

  /**
   * setAny.
   * @param anyParam any
   */
  public void setAny(final Attributes anyParam) {
    this.any = anyParam;
  }

  /**
   * setPost.
   * @param postParam post effect or not
   */
  public void setPost(final boolean postParam) {
    this.post = postParam;
  }

  /**
   * Create effect.
   * @param element element
   * @param nifty nifty
   * @param effectEventId eventId
   * @param registeredEffects effects
   * @param time time
   */
  public void create(
      final de.lessvoid.nifty.elements.Element element,
      final Nifty nifty,
      final EffectEventId effectEventId,
      final Map < String, RegisterEffectType > registeredEffects,
      final TimeProvider time) {
    RegisterEffectType registerEffectType = registeredEffects.get(name);
    if (registerEffectType == null) {
      log.warning("unable to convert effect [" + name + "] because no effect with this name has been registered.");
      return;
    }
    Class < ? > effectClass = registerEffectType.getEffectClass();
    if (effectClass == null) {
      log.warning("unable to convert effect [" + name + "] because no effect with this name has been registered.");
      return;
    }

    // create the effect class
    if (effectEventId.equals(EffectEventId.onHover)) {
      HoverEffect hoverEffect = new HoverEffect(nifty, inherit, post, alternateKey, alternateEnable);
      hoverEffect.init(
          element,
          createHoverEffectImpl(effectClass),
          any.createProperties(),
          time);
      element.registerEffect(effectEventId, hoverEffect);
    } else {
      StaticEffect effect = new StaticEffect(nifty, inherit, post, alternateKey, alternateEnable);
      effect.init(
          element,
          createEffectImpl(effectClass),
          any.createProperties(),
          time);
      element.registerEffect(effectEventId, effect);
    }
  }

  /**
   * Create the HoverEffect.
   * @param effectClass class
   * @return HoverEffectImpl
   */
  private HoverEffectImpl createHoverEffectImpl(final Class < ? > effectClass) {
    try {
      if (HoverEffectImpl.class.isAssignableFrom(effectClass)) {
        return (HoverEffectImpl) effectClass.newInstance();
      } else {
        log.warning(
            "given effect class [" + effectClass.getName()
            + "] does not implement ["
            + HoverEffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + effectClass + "] could not be instanziated");
    }
    return null;
  }

  /**
   * create EffectImpl.
   * @param effectClass the className to instantiate
   * @return new EffectImpl instance
   */
  private EffectImpl createEffectImpl(final Class < ? > effectClass) {
    try {
      if (EffectImpl.class.isAssignableFrom(effectClass)) {
        return (EffectImpl) effectClass.newInstance();
      } else {
        log.warning("given effect class ["
            + effectClass.getName()
            + "] does not implement ["
            + EffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + effectClass.getName() + "] could not be instanziated");
    }
    return null;
  }

}
