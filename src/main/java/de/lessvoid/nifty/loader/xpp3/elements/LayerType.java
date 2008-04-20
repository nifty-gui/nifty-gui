package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;


/**
 * LayerType.
 * @author void
 */
public class LayerType implements XmlElementProcessor {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * screen.
   */
  private Screen screen;

  /**
   * effects.
   */
  private Map < String, Class < ? > > registeredEffects;

  /**
   * LayerType.
   * @param niftyParam nifty
   * @param screenParam screenParam
   * @param registeredEffectsParam registeredEffectsParam
   */
  public LayerType(
      final Nifty niftyParam,
      final Screen screenParam,
      final Map < String, Class < ? > > registeredEffectsParam) {
    nifty = niftyParam;
    screen = screenParam;
    this.registeredEffects = registeredEffectsParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    Element rootPanel = NiftyCreator.createLayer(nifty, screen, attributes);
    screen.addLayerElement(rootPanel);

    NiftyCreator.processElementAttributes(nifty, rootPanel, attributes);
    ElementType.processChildElements(xmlParser, nifty, screen, rootPanel, registeredEffects);
  }
}
