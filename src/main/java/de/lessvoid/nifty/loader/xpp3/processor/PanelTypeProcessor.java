package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.PanelType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;

/**
 * PanelType.
 * @author void
 */
public class PanelTypeProcessor implements XmlElementProcessor {

  /**
   * the element this belongs to.
   */
  private ElementType element;

  /**
   * create it.
   * @param elementParam elementParam
   */
  public PanelTypeProcessor(final ElementType elementParam) {
    this.element = elementParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    PanelType panel = new PanelType();
    ProcessorHelper.processElement(xmlParser, panel, attributes);
    element.addElementType(panel);

    /*
    PanelRenderer renderer = NiftyCreator.createPanelRenderer(nifty.getRenderDevice(), attributes);
    Element panel = new Element(
        attributes.get("id"),
        parent,
        screen,
        false,
        renderer);
    NiftyCreator.processElementAttributes(nifty, panel, attributes);
    parent.add(panel);

    ElementHelper.processChildElements(xmlParser, nifty, screen, panel, registeredEffects, screenController);
    */
  }
}
