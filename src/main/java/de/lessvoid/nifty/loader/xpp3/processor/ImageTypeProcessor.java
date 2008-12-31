package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.ImageType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

/**
 * ImageType.
 * @author void
 */
public class ImageTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;
  private TypeContext typeContext;

  /**
   * init it.
   * @param typeContext TODO
   * @param elementParam element
   */
  public ImageTypeProcessor(final TypeContext newTypeContext, final ElementType elementParam) {
    this.typeContext = newTypeContext;
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    ImageType imageType = typeContext.createImageType(attributes);
    ProcessorHelper.processElement(xmlParser, imageType, typeContext);
    element.addElementType(imageType);
  }
}
