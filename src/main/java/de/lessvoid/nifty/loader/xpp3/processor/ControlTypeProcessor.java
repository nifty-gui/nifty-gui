package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ControlType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;


/**
 * ControlTypeProcessor.
 * @author void
 */
public class ControlTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;

  /**
   * init it.
   * @param elementParam element
   */
  public ControlTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    ControlType controlType = new ControlType(attributes.get("name"));
    if (attributes.isSet("onChange")) {
      controlType.setOnChange(attributes.get("onChange"));
    }
    ProcessorHelper.processElement(xmlParser, controlType, attributes);
    element.addElementType(controlType);
  }
}
