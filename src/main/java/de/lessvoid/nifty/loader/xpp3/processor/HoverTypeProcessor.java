package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.HoverType;

/**
 * HoverType.
 * @author void
 */
public class HoverTypeProcessor implements XmlElementProcessor {

  /**
   * element this belongs to.
   */
  private ElementType element;

  /**
   * HoverType.
   */
  private HoverType hoverType;

  /**
   * init it.
   * @param elementParam element
   */
  public HoverTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    hoverType = new HoverType();
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
    element.setHover(hoverType);
    xmlParser.nextTag();
  }
}
