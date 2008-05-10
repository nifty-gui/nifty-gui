package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControlController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * ImageType.
 * @author void
 */
public class MenuType extends ElementType {

  /**
   * filename.
   * @required
   */
  private String font;

  /**
   * create it.
   * @param fontParam filename
   */
  public MenuType(final String fontParam) {
    this.font = fontParam;
  }

  /**
   * create element.
   * @param parent parent
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param time time
   * @param controlController controlController
   * @param screenController screenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time,
      final ControlController controlController,
      final ScreenController screenController) {
    Element element = NiftyCreator.createPanel(
        getId(),
        nifty,
        screen,
        parent,
        getBackgroundImage(),
        getBackgroundColor().createColor(),
        false);
    super.addElementAttributes(
        element,
        screen,
        nifty,
        registeredEffects,
        registeredControls,
        time,
        controlController,
        screenController);
    parent.add(element);
    return element;
  }
}
