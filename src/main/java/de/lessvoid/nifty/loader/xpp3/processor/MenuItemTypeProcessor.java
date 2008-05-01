package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ColorType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.MenuItemType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;

/**
 * PanelType.
 * @author void
 */
public class MenuItemTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;

  /**
   * font.
   */
  private String font;

  /**
   * create it.
   * @param elementParam element
   * @param fontParam font
   */
  public MenuItemTypeProcessor(final ElementType elementParam, final String fontParam) {
    this.element = elementParam;
    this.font = fontParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    MenuItemType menuItemType = new MenuItemType(font);
    if (attributes.isSet("text")) {
      menuItemType.setText(attributes.get("text"));
    }
    if (attributes.isSet("color")) {
      menuItemType.setColor(new ColorType(attributes.get("color")));
    }
    ProcessorHelper.processElement(xmlParser, menuItemType, attributes);
    element.addElementType(menuItemType);
  }
}
