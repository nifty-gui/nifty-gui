package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.LabelType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;

/**
 * LabelTypeProcessor.
 * @author void
 */
public class LabelTypeProcessor implements XmlElementProcessor {

  /**
   * element this belongs to.
   */
  private ElementType elementType;

  /**
   * init it.
   * @param elementParam element
   */
  public LabelTypeProcessor(final ElementType elementParam) {
    this.elementType = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    LabelType labelType = new LabelType(attributes.get("text"));
    ProcessorHelper.processElement(xmlParser, labelType, attributes);
    elementType.addElementType(labelType);
  }
}
