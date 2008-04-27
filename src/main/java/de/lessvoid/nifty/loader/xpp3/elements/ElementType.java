package de.lessvoid.nifty.loader.xpp3.elements;


import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.SubstitutionGroup;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ElementType.
 * @author void
 */
public class ElementType implements XmlElementProcessor {

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
  }

  /**
   * @param xmlParser xmlParser
   * @param nifty nifty
   * @param screen screen
   * @param element panel
   * @param registeredEffectsParam registeredEffectsParam
   * @param screenController ScreenController
   * @throws Exception exception
   */
  public static void processChildElements(
      final XmlParser xmlParser,
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Map < String, Class < ? > > registeredEffectsParam,
      final ScreenController screenController) throws Exception {
    xmlParser.nextTag();
    xmlParser.optional("interact", new InteractType(element, screenController));
    xmlParser.optional("hover", new HoverType(element));
    xmlParser.optional("effect", new EffectsType(nifty, registeredEffectsParam, element));
    xmlParser.zeroOrMore(
          new SubstitutionGroup().
            add("panel", new PanelType(nifty, screen, element, registeredEffectsParam, screenController)).
            add("text", new TextType(nifty, screen, element, registeredEffectsParam, screenController)).
            add("image", new ImageType(nifty, screen, element, registeredEffectsParam, screenController)).
            add("menu", new MenuType(nifty, screen, element, registeredEffectsParam, screenController))
            );
  }
}
