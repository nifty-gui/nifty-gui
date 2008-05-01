package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.EffectType;
import de.lessvoid.nifty.loader.xpp3.elements.EffectsType;


/**
 * EffectType.
 * @author void
 */
public class EffectTypeProcessor implements XmlElementProcessor {

  /**
   * EffectEventId.
   */
  private EffectEventId effectEventId;

  /**
   * effects type.
   */
  private EffectsType effectsType;

  /**
   * create.
   * @param effectsTypeParam EffectsType
   * @param effectEventIdParam effectEventIdParam
   */
  public EffectTypeProcessor(final EffectsType effectsTypeParam, final EffectEventId effectEventIdParam) {
    this.effectsType = effectsTypeParam;
    this.effectEventId = effectEventIdParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    EffectType effectType = new EffectType(attributes);
    if (attributes.isSet("name")) {
      effectType.setName(attributes.get("name"));
    }
    if (attributes.isSet("inherit")) {
      effectType.setInherit(attributes.getAsBoolean("inherit"));
    }
    if (attributes.isSet("post")) {
      effectType.setPost(attributes.getAsBoolean("post"));
    }
    if (attributes.isSet("alternateEnable")) {
      effectType.setAlternateKey(attributes.get("alternateEnable"));
      effectType.setAlternateEnable(true);
    } else if (attributes.isSet("alternateDisable")) {
      effectType.setAlternateKey(attributes.get("alternateDisable"));
      effectType.setAlternateEnable(false);
    }
    if (effectEventId == EffectEventId.onStartScreen) {
      effectsType.addOnStartScreen(effectType);
    } else if (effectEventId == EffectEventId.onEndScreen) {
      effectsType.addOnEndScreen(effectType);
    } else if (effectEventId == EffectEventId.onClick) {
      effectsType.addOnClick(effectType);
    } else if (effectEventId == EffectEventId.onFocus) {
      effectsType.addOnFocus(effectType);
    } else if (effectEventId == EffectEventId.onHover) {
      effectsType.addOnHover(effectType);
    } else if (effectEventId == EffectEventId.onActive) {
      effectsType.addOnActive(effectType);
    }

    xmlParser.nextTag();
  }
}
