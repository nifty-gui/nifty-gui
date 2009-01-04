package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.SubstitutionGroup;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.EffectsType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.StyleType;

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
   * StyleType.
   */
  private StyleType style;

  /**
   * init with StyleType.
   * @param styleParam StyleType
   */
  public EffectsTypeProcessor(final StyleType styleParam) {
    style = styleParam;
  }

  /**
   * init it.
   * @param elementParam element
   */
  public EffectsTypeProcessor(final ElementType elementParam) {
    element = elementParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    EffectsType effectsType = new EffectsType(attributes.getAsBoolean("overlay", false));
    xmlParser.nextTag();
    xmlParser.zeroOrMore(
        new SubstitutionGroup().
          add("onStartScreen", new EffectTypeProcessor(effectsType, EffectEventId.onStartScreen)).
          add("onEndScreen", new EffectTypeProcessor(effectsType, EffectEventId.onEndScreen)).
          add("onHover", new EffectTypeProcessor(effectsType, EffectEventId.onHover)).
          add("onClick", new EffectTypeProcessor(effectsType, EffectEventId.onClick)).
          add("onFocus", new EffectTypeProcessor(effectsType, EffectEventId.onFocus)).
          add("onGetFocus", new EffectTypeProcessor(effectsType, EffectEventId.onGetFocus)).
          add("onLostFocus", new EffectTypeProcessor(effectsType, EffectEventId.onLostFocus)).
          add("onActive", new EffectTypeProcessor(effectsType, EffectEventId.onActive)).
          add("onShow", new EffectTypeProcessor(effectsType, EffectEventId.onShow)).
          add("onHide", new EffectTypeProcessor(effectsType, EffectEventId.onHide)).
          add("onCustom", new EffectTypeProcessor(effectsType, EffectEventId.onCustom))
          );

    if (element != null) {
      element.setEffects(effectsType);
    }

    if (style != null) {
      style.setEffects(effectsType);
    }
  }
}
