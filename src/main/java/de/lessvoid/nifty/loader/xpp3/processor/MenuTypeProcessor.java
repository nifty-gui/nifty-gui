package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.MenuType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

/**
 * PanelType.
 * @author void
 */
public class MenuTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;
  private TypeContext typeContext;

  /**
   * create it.
   * @param elementParam element
   */
  public MenuTypeProcessor(final TypeContext newTypeContext, final ElementType elementParam) {
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
    String font = attributes.get("font");
    MenuType menuType = new MenuType(typeContext);
    ProcessorHelper.processElement(xmlParser, menuType, attributes, typeContext);
    xmlParser.zeroOrMore("menuItem", new MenuItemTypeProcessor(typeContext, menuType, new FocusHandler(), font));
    xmlParser.zeroOrMore("control", new ControlTypeProcessor(typeContext, menuType));
    element.addElementType(menuType);
  }
}
