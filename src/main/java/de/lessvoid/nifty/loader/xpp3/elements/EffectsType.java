package de.lessvoid.nifty.loader.xpp3.elements;


import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * EffectsType.
 * @author void
 */
public class EffectsType implements XmlElementProcessor {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * element.
   */
  private Element element;

  /**
   * effects.
   */
  private Map < String, Class < ? > > registeredEffects;

  /**
   * create.
   * @param niftyParam niftyParam
   * @param registeredEffectsParam registeredEffectsParam
   * @param elementParam elementParam
   */
  public EffectsType(
      final Nifty niftyParam,
      final Map < String, Class < ? > > registeredEffectsParam,
      final Element elementParam) {
    this.nifty = niftyParam;
    this.registeredEffects = registeredEffectsParam;
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String effectGroup = attributes.get("effectGroup");
    String effectGroupOverride = attributes.get("effectGroupOverride");

    xmlParser.nextTag();
    xmlParser.zeroOrMore(
        "onStartScreen", new EffectType(nifty, registeredEffects, element, EffectEventId.onStartScreen));
    xmlParser.zeroOrMore(
        "onEndScreen", new EffectType(nifty, registeredEffects, element, EffectEventId.onEndScreen));
    xmlParser.zeroOrMore(
        "onHover", new EffectType(nifty, registeredEffects, element, EffectEventId.onHover));
    xmlParser.zeroOrMore(
        "onFocus", new EffectType(nifty, registeredEffects, element, EffectEventId.onFocus));
    xmlParser.zeroOrMore(
        "onActive", new EffectType(nifty, registeredEffects, element, EffectEventId.onActive));
  }
}
