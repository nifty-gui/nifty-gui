package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.AttributesType;
import de.lessvoid.nifty.loader.xpp3.elements.StyleType;


/**
 * EffectType.
 * @author void
 */
public class StyleAttributesTypeProcessor implements XmlElementProcessor {

  /**
   * EffectEventId.
   */
  private StyleType styleType;

  /**
   * create.
   * @param styleTypeParam StyleType
   */
  public StyleAttributesTypeProcessor(final StyleType styleTypeParam) {
    styleType = styleTypeParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    AttributesType attributesType = new AttributesType(attributes);
    styleType.setAttributes(attributesType);
    xmlParser.nextTag();
  }
}
