package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.MenuItemType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

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
   * default font (inherited from parent menu).
   */
  private String defaultFont;

  private TypeContext typeContext;

  /**
   * create it.
   * @param elementParam element
   * @param focusHandlerParam focusHandlerParam
   * @param fontParam font
   */
  public MenuItemTypeProcessor(
      final TypeContext newTypeContext,
      final ElementType elementParam,
      final FocusHandler focusHandlerParam,
      final String fontParam) {
    this.typeContext = newTypeContext;
    this.element = elementParam;
    this.defaultFont = fontParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    MenuItemType menuItemType = new MenuItemType(typeContext);
    menuItemType.setText(attributes.get("text"));
    if (!attributes.isSet("font")) {
      attributes.overwriteAttribute("font", defaultFont);
    }
    ProcessorHelper.processElement(xmlParser, menuItemType, attributes, typeContext);
    element.addElementType(menuItemType);
  }
}
