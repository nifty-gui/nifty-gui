package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.TextType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

/**
 * PanelType.
 * @author void
 */
public class TextTypeProcessor implements XmlElementProcessor {

  /**
   * element this belongs to.
   */
  private ElementType elementType;
  private TypeContext typeContext;

  /**
   * init it.
   * @param elementParam element
   */
  public TextTypeProcessor(final TypeContext newTypeContext, final ElementType elementParam) {
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
    TextType textType = new TextType(typeContext, attributes.get("text"));
    ProcessorHelper.processElement(xmlParser, textType, attributes, typeContext);
    xmlParser.zeroOrMore("textSelection", new TextSelectionTypeProcessor(textType));
    elementType.addElementType(textType);
  }
}
