package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;


/**
 * LayerType.
 * @author void
 */
public class LayerType extends PanelType {

  /**
   * Create Layer.
   * @param nifty nifty
   * @param screen screen
   * @param screenController screenController
   * @param registeredEffects effects
   * @param registeredControls registeredControls
   * @param time time
   * @return element element
   */
  public Element createElement(
      final Nifty nifty,
      final Screen screen,
      final ScreenController screenController,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time) {
    Element layer = NiftyCreator.createLayer(
        getId(),
        nifty,
        screen,
        getBackgroundImage(),
        getBackgroundColor().createColor());
    super.addElementAttributes(layer, screen, screenController, nifty, registeredEffects, registeredControls, time);
    return layer;
  }
}
