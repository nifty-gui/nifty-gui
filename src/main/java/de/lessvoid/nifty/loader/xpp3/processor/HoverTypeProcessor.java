package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.EffectType;
import de.lessvoid.nifty.loader.xpp3.elements.HoverType;

/**
 * HoverType.
 * @author void
 */
public class HoverTypeProcessor implements XmlElementProcessor {
  private EffectType effectType;

  public HoverTypeProcessor(final EffectType effectTypeParam) {
    effectType = effectTypeParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    HoverType hoverType = new HoverType();
    if (attributes.isSet("width")) {
      hoverType.setWidth(attributes.get("width"));
    }
    if (attributes.isSet("height")) {
      hoverType.setHeight(attributes.get("height"));
    }
    if (attributes.isSet("falloffType")) {
      hoverType.setFalloffType(attributes.getAsHoverFalloffType("falloffType"));
    }
    if (attributes.isSet("falloffConstraint")) {
      hoverType.setFalloffConstraint(attributes.getAsHoverFalloffConstraintType("falloffConstraint"));
    }
    if (effectType != null) {
      effectType.setHover(hoverType);
    }
    xmlParser.nextTag();
  }
}
