package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.MenuType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;

/**
 * PanelType.
 * @author void
 */
public class MenuTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;

  /**
   * create it.
   * @param elementParam element
   */
  public MenuTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String font = attributes.get("font");
    MenuType menuType = new MenuType(font);
    ProcessorHelper.processElement(xmlParser, menuType, attributes);
    xmlParser.zeroOrMore("menuItem", new MenuItemTypeProcessor(menuType, font));
    element.addElementType(menuType);
  }
}
