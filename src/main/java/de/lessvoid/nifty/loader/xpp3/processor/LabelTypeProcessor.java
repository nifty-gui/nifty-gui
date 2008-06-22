package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.LabelType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

/**
 * LabelTypeProcessor.
 * @author void
 */
public class LabelTypeProcessor implements XmlElementProcessor {

  /**
   * element this belongs to.
   */
  private ElementType elementType;
  private TypeContext typeContext;

  /**
   * init it.
   * @param elementParam element
   */
  public LabelTypeProcessor(final TypeContext newTypeContext, final ElementType elementParam) {
    this.typeContext = newTypeContext;
    this.elementType = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    LabelType labelType = new LabelType(typeContext, attributes.get("text"));
    if (!attributes.isSet("style")) {
      attributes.set("style", "nifty-label");
    }
    ProcessorHelper.processElement(xmlParser, labelType, attributes, typeContext);
    elementType.addElementType(labelType);
  }
}
