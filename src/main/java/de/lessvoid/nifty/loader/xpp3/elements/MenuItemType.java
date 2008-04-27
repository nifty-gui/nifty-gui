package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
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
public class MenuItemType implements XmlElementProcessor {

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
   * font.
   */
  private String font;

  /**
   * LayerType.
   * @param niftyParam nifty
   * @param screenParam screenParam
   * @param parentParam parentParam
   * @param registeredEffectsParam registeredEffectsParam
   * @param screenControllerParam ScreenController
   * @param fontParam font
   */
  public MenuItemType(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element parentParam,
      final Map < String, Class < ? > > registeredEffectsParam,
      final ScreenController screenControllerParam,
      final String fontParam) {
    nifty = niftyParam;
    screen = screenParam;
    parent = parentParam;
    this.registeredEffects = registeredEffectsParam;
    this.screenController = screenControllerParam;
    this.font = fontParam;
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
          nifty.getRenderDevice().createFont(font),
          attributes.get("text"),
          new Color(attributes.get("color")));
    } else {
      textRenderer = new TextRenderer(
          nifty.getRenderDevice().createFont(font),
          attributes.get("text"));
    }
    PanelRenderer panelRenderer = NiftyCreator.createPanelRenderer(nifty.getRenderDevice(), attributes);
    Element current = new Element(
        attributes.get("id"),
        parent,
        screen,
        true,
        panelRenderer,
        textRenderer);
    current.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
    current.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
    if (attributes.isSet("align")) {
      current.setConstraintHorizontalAlign(HorizontalAlign.valueOf(attributes.get("align").toString()));
    }

    if (attributes.isSet("valign")) {
      current.setConstraintVerticalAlign(VerticalAlign.valueOf(attributes.get("valign").toString()));
    }

    NiftyCreator.processElementAttributes(nifty, current, attributes);
    parent.add(current);

    parent.setConstraintWidth(getMenuMaxWidth(parent.getElements()));
    parent.setConstraintHeight(getMenuMaxHeight(parent.getElements()));

    ElementType.processChildElements(xmlParser, nifty, screen, current, registeredEffects, screenController);
  }

  /**
   * get max height of all elements.
   * @param elements the elements to check
   * @return max height
   */
  private static SizeValue getMenuMaxHeight(final List < Element > elements) {
    if (elements == null || elements.isEmpty()) {
      return null;
    }

    int sum = 0;
    for (Element e : elements) {
      SizeValue current = e.getConstraintHeight();
      if (current.isPercentOrPixel()) {
        sum += current.getValueAsInt(0);
      }
    }
    return new SizeValue(sum + "px");
  }

  /**
   * get max width of all elements.
   * @param elements the elements to check
   * @return max width
   */
  private static SizeValue getMenuMaxWidth(final List < Element > elements) {
    if (elements == null || elements.isEmpty()) {
      return null;
    }

    int max = -1;
    for (Element e : elements) {
      SizeValue current = e.getConstraintWidth();
      if (current.isPercentOrPixel()) {
        int value = current.getValueAsInt(0);
        if (value > max) {
          max = value;
        }
      }
    }
    return new SizeValue(max + "px");
  }
  
}
