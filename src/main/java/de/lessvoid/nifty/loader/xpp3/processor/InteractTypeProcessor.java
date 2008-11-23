package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.InteractType;

/**
 * InteractType.
 * @author void
 */
public class InteractTypeProcessor implements XmlElementProcessor {

  /**
   * interact type.
   */
  private InteractType interactType;

  /**
   * element.
   */
  private ElementType element;

  /**
   * create it.
   * @param elementParam element
   */
  public InteractTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    interactType = new InteractType();
    if (attributes.isSet("onClick")) {
      interactType.setOnClick(attributes.getAsOnClickType("onClick"));
    }
    if (attributes.isSet("onRelease")) {
      interactType.setOnRelease(attributes.getAsOnClickType("onRelease"));
    }
    if (attributes.isSet("onClickRepeat")) {
      interactType.setOnClickRepeat(attributes.getAsOnClickType("onClickRepeat"));
    }
    if (attributes.isSet("onClickMouseMove")) {
      interactType.setOnClickMouseMove(attributes.getAsOnClickType("onClickMouseMove"));
    }
    if (attributes.isSet("onClickAlternateKey")) {
      interactType.setOnClickAlternateKey(attributes.get("onClickAlternateKey"));
    }
    element.setInteract(interactType);
    xmlParser.nextTag();
  }
}
