package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;


/**
 * PopupType.
 * @author void
 */
public class PopupType extends PanelType {

  private String controller;

  public PopupType(final TypeContext typeContext, final Attributes attributesParam, final String controllerParam) {
    super(typeContext, attributesParam);
    controller = controllerParam;
  }

  public Controller getControllerInstance(final Nifty nifty) {
    return ClassHelper.getInstance(controller, Controller.class);
  }

  /**
   * Create Layer.
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects effects
   * @param registeredControls registeredControls
   * @param styleHandler style handler
   * @param time time
   * @param inputControl input control
   * @param screenController screen controller
   * @return element
   */
  public Element createElement(
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    Element element = NiftyCreator.createLayer(
        null,
        this,
        getAttributes().getId(),
        nifty,
        screen,
        getAttributes(),
        typeContext.time);
    super.addAllElementAttributes(
        element,
        screen,
        screenController,
        inputControl);
    return element;
  }
}
