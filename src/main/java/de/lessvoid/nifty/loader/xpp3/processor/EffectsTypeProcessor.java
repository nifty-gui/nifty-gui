package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.EffectsType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;

/**
 * EffectsType.
 * @author void
 */
public class EffectsTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;

  /**
   * effects type.
   */
  private EffectsType effectsType;

  /**
   * init it.
   * @param elementParam element
   */
  public EffectsTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    effectsType = new EffectsType();

    if (attributes.isSet("effectGroup")) {
      effectsType.setEffectGroup(attributes.get("effectGroup"));
    }

    if (attributes.isSet("effectGroupOverride")) {
      effectsType.setEffectGroupOverride(attributes.get("effectGroupOverride"));
    }

    xmlParser.nextTag();
    xmlParser.zeroOrMore("onStartScreen", new EffectTypeProcessor(effectsType, EffectEventId.onStartScreen));
    xmlParser.zeroOrMore("onEndScreen", new EffectTypeProcessor(effectsType, EffectEventId.onEndScreen));
    xmlParser.zeroOrMore("onHover", new EffectTypeProcessor(effectsType, EffectEventId.onHover));
    xmlParser.zeroOrMore("onClick", new EffectTypeProcessor(effectsType, EffectEventId.onClick));
    xmlParser.zeroOrMore("onFocus", new EffectTypeProcessor(effectsType, EffectEventId.onFocus));
    xmlParser.zeroOrMore("onActive", new EffectTypeProcessor(effectsType, EffectEventId.onActive));

    element.setEffects(effectsType);
  }
}
