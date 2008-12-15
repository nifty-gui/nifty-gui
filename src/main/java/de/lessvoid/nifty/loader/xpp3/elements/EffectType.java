package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
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
   * default constructor.
   */
  public EffectType() {
  }

  /**
   * create it.
   * @param attributes attributes
   */
  public void initFromAttributes(final Attributes attributes) {
    any = attributes;

    if (attributes.isSet("name")) {
      setName(attributes.get("name"));
    }
    if (attributes.isSet("inherit")) {
      setInherit(attributes.getAsBoolean("inherit"));
    }
    if (attributes.isSet("post")) {
      setPost(attributes.getAsBoolean("post"));
    }
    if (attributes.isSet("alternateEnable")) {
      setAlternateKey(attributes.get("alternateEnable"));
      setAlternateEnable(true);
    } else if (attributes.isSet("alternateDisable")) {
      setAlternateKey(attributes.get("alternateDisable"));
      setAlternateEnable(false);
    }
  }

  /**
   * copy.
   * @param source source
   */
  public EffectType(final EffectType source) {
    this.name = source.name;
    this.post = source.post;
    this.alternateKey = source.alternateKey;
    this.alternateEnable = source.alternateEnable;
    this.inherit = source.inherit;
    this.any = new Attributes(source.any);
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
   * getAny.
   * @return attributes
   */
  public Attributes getAny() {
    return any;
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
    Effect effect = null;
    if (effectEventId.equals(EffectEventId.onHover)) {
      effect = new Effect(nifty, inherit, post, alternateKey, alternateEnable, effectEventId);
      effect.enableHover();
      effect.enableInfinite();
    } else {
      effect = new Effect(nifty, inherit, post, alternateKey, alternateEnable, effectEventId);
    }

    if (effectEventId.equals(EffectEventId.onFocus)) {
      effect.enableInfinite();
    }

    effect.init(element, createEffectImpl(effectClass), any.createProperties(), time);
    element.registerEffect(effectEventId, effect);
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
