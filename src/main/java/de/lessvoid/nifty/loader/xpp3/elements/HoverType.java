package de.lessvoid.nifty.loader.xpp3.elements;


import java.util.Properties;

import de.lessvoid.nifty.effects.shared.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * HoverType.
 * @author void
 */
public class HoverType implements XmlElementProcessor {

  /**
   * the element.
   */
  private Element element;

  /**
   * create.
   * @param elementParam element
   */
  public HoverType(final Element elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    Properties prop = new Properties();
    if (attributes.isSet("width")) {
      prop.put(Falloff.HOVER_WIDTH, attributes.get("width"));
    }
    if (attributes.isSet("height")) {
      prop.put(Falloff.HOVER_HEIGHT, attributes.get("height"));
    }
    if (attributes.isSet("falloffType")) {
      prop.put(Falloff.HOVER_FALLOFF_TYPE, attributes.get("falloffType"));
    }
    if (attributes.isSet("falloffConstraint")) {
      prop.put(Falloff.HOVER_FALLOFF_CONSTRAINT, attributes.get("falloffConstraint"));
    }
    if (!prop.isEmpty()) {
      Falloff falloff = new Falloff(prop);
      element.setHotSpotFalloff(falloff);
    }
    xmlParser.nextTag();
  }
}
