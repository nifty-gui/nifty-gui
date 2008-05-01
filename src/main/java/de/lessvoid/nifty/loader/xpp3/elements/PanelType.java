package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * PanelType.
 * @author void
 */
public class PanelType extends ElementType {

  /**
   * create element.
   * @param parent parent parent
   * @param nifty nifty
   * @param screen screen
   * @param screenController screenController
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param time time
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Object screenController,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time) {
    Element element = NiftyCreator.createPanel(
        getId(),
        nifty,
        screen,
        parent,
        getBackgroundImage(),
        getBackgroundColor().createColor(),
        false);
    super.addElementAttributes(element, screen, screenController, nifty, registeredEffects, registeredControls, time);
    parent.add(element);
    return element;
  }
}
