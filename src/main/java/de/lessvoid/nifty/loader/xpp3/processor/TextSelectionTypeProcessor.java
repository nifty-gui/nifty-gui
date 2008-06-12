package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.TextType;

/**
 * PanelType.
 * @author void
 */
public class TextSelectionTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private TextType element;

  /**
   * create it.
   * @param elementParam element
   */
  public TextSelectionTypeProcessor(final TextType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    if (attributes.isSet("textColor")) {
      element.setTextSelectionTextColor(attributes.get("textColor"));
    }
    xmlParser.nextTag();
  }
}
