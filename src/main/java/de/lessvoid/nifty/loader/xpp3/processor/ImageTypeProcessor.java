package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.ImageType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;

/**
 * ImageType.
 * @author void
 */
public class ImageTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;

  /**
   * init it.
   * @param elementParam element
   */
  public ImageTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    ImageType imageType = new ImageType(attributes.get("filename"));
    if (attributes.isSet("filter")) {
      imageType.setFilter(attributes.getAsBoolean("filer"));
    }
    if (attributes.isSet("subImageSizeMode")) {
      imageType.setSubImageSizeMode(attributes.getAsSubImageSizeModeType("subImageSizeMode"));
    }
    if (attributes.isSet("resizeHint")) {
      imageType.setResizeHint(attributes.get("resizeHint"));
    }
    ProcessorHelper.processElement(xmlParser, imageType, attributes);
    element.addElementType(imageType);
  }
}
