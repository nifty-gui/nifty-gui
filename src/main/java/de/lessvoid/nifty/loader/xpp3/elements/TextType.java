package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * PanelType.
 * @author void
 */
public class TextType implements XmlElementProcessor {

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
  public TextType(
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
    TextRenderer textRenderer;
    if (attributes.isSet("color")) {
      textRenderer = new TextRenderer(
          nifty.getRenderDevice().createFont(attributes.get("font")),
          attributes.get("text"),
          new Color(attributes.get("color")));
    } else {
      textRenderer = new TextRenderer(
          nifty.getRenderDevice().createFont(attributes.get("font")),
          attributes.get("text"));
    }
    PanelRenderer panelRenderer = NiftyCreator.createPanelRenderer(nifty.getRenderDevice(), attributes);
    Element textPanel = new Element(
        attributes.get("id"),
        parent,
        screen,
        false,
        panelRenderer,
        textRenderer);
    textPanel.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
    textPanel.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
    NiftyCreator.processElementAttributes(nifty, textPanel, attributes);
    parent.add(textPanel);

    ElementType.processChildElements(xmlParser, nifty, screen, textPanel, registeredEffects, screenController);
  }
}
