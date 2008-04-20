package de.lessvoid.nifty.loader.xpp3.elements;


import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.general.EffectImpl;
import de.lessvoid.nifty.effects.general.StaticEffect;
import de.lessvoid.nifty.effects.hover.HoverEffect;
import de.lessvoid.nifty.effects.hover.HoverEffectImpl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * EffectType.
 * @author void
 */
public class EffectType implements XmlElementProcessor {

  /**
   * logger.
   */
  private Logger log = Logger.getLogger(EffectType.class.getName());

  /**
   * registered effects.
   */
  private Map < String, Class < ? >> registerEffects;

  /**
   * EffectEventId.
   */
  private EffectEventId effectEventId;

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * the element.
   */
  private Element element;

  /**
   * default value for post/pre effect state.
   */
  private static final boolean DEFAULT_EFFECT_POST = false;

  /**
   * default value for alternate key.
   */
  private static final String DEFAULT_ALTERNATE_KEY = null;

  /**
   * create.
   * @param niftyParam nifty
   * @param registerEffectsParam registered effects
   * @param elementParam elementParam
   * @param effectEventIdParam effectEventIdParam
   */
  public EffectType(
      final Nifty niftyParam,
      final Map < String, Class < ? > > registerEffectsParam,
      final Element elementParam,
      final EffectEventId effectEventIdParam) {
    this.nifty = niftyParam;
    this.registerEffects = registerEffectsParam;
    this.element = elementParam;
    this.effectEventId = effectEventIdParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    xmlParser.nextTag();

    String name = attributes.get("name");
    Class < ? > effectClass = registerEffects.get(name);
    if (effectClass == null) {
      log.warning("unable to convert effect [" + name + "] because no effect with this name has been registered.");
      return;
    }

    // get inherit
    boolean inherit = false;
    if (attributes.isSet("inherit")) {
      inherit = "true".equals(attributes.get("inherit"));
    }

    // get post mode and default to post = true, when nothing is given
    boolean post = DEFAULT_EFFECT_POST;
    if (attributes.isSet("post")) {
      if ("false".equals(attributes.get("post"))) {
        post = false;
      } else if ("true".equals(attributes.get("post"))) {
        post = true;
      }
    }

    // get alternate key, this defaults to null
    String alternateKey = DEFAULT_ALTERNATE_KEY;
    boolean alternateEnable = false;
    if (attributes.isSet("alternateEnable")) {
      alternateKey = attributes.get("alternateEnable");
      alternateEnable = true;
    } else if (attributes.isSet("alternateDisable")) {
      alternateKey = attributes.get("alternateDisable");
      alternateEnable = false;
    }

    // create the effect class
    if (effectEventId.equals(EffectEventId.onHover)) {
      HoverEffect hoverEffect = new HoverEffect(nifty, inherit, post, alternateKey, alternateEnable);
      hoverEffect.init(
          element,
          createHoverEffectImpl(effectClass),
          attributes.createProperties(),
          new TimeProvider());
      element.registerEffect(effectEventId, hoverEffect);
    } else {
      StaticEffect effect = new StaticEffect(nifty, inherit, post, alternateKey, alternateEnable);
      effect.init(
          element,
          createEffectImpl(effectClass),
          attributes.createProperties(),
          new TimeProvider());
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
