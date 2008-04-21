package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * PanelType.
 * @author void
 */
public class PanelType implements XmlElementProcessor {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * screen.
   */
  private Screen screen;

  /**
   * parent.
   */
  private Element parent;

  /**
   * effects.
   */
  private Map < String, Class < ? > > registeredEffects;

  /**
   * ScreenController.
   */
  private ScreenController screenController;

  /**
   * LayerType.
   * @param niftyParam nifty
   * @param screenParam screenParam
   * @param parentParam parentParam
   * @param registeredEffectsParam registeredEffectsParam
   * @param screenControllerParam ScreenController
   */
  public PanelType(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element parentParam,
      final Map < String, Class < ? > > registeredEffectsParam,
      final ScreenController screenControllerParam) {
    nifty = niftyParam;
    screen = screenParam;
    parent = parentParam;
    this.registeredEffects = registeredEffectsParam;
    this.screenController = screenControllerParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    PanelRenderer renderer = NiftyCreator.createPanelRenderer(nifty.getRenderDevice(), attributes);
    Element panel = new Element(
        attributes.get("id"),
        parent,
        screen,
        false,
        renderer);
    NiftyCreator.processElementAttributes(nifty, panel, attributes);
    parent.add(panel);

    ElementType.processChildElements(xmlParser, nifty, screen, panel, registeredEffects, screenController);
  }
}
