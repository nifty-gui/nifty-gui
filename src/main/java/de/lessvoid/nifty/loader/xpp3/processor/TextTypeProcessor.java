package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ColorType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.TextType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;

/**
 * PanelType.
 * @author void
 */
public class TextTypeProcessor implements XmlElementProcessor {

  /**
   * element this belongs to.
   */
  private ElementType element;

  /**
   * init it.
   * @param elementParam element
   */
  public TextTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    TextType textType = new TextType(attributes.get("text"), attributes.get("font"));
    if (attributes.isSet("color")) {
      textType.setColor(new ColorType(attributes.get("color")));
    }
    ProcessorHelper.processElement(xmlParser, textType, attributes);
    element.addElementType(textType);
  }
}
