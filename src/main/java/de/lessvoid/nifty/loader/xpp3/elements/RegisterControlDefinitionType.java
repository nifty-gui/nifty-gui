package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.mapping.Default;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * RegisterControlDefinitionType.
 * @author void
 */
public class RegisterControlDefinitionType {
  /**
   * log.
   */
  private static Logger log = Logger.getLogger(RegisterControlDefinitionType.class.getName());

  /**
   * name.
   */
  private String name;

  /**
   * controller.
   */
  private String controller;

  /**
   * inputMapper.
   */
  private String inputMapper;

  /**
   * attributes.
   */
  private Attributes controlDefinitionAttributes;

  /**
   * elements.
   */
  private Collection < ElementType > elements = new ArrayList < ElementType >();

  /**
   * Create new instance.
   * @param nameParam name
   * @param controllerParam controller
   * @param inputMapperParam inputMapperParam
   * @param newAttributes attributes
   */
  public RegisterControlDefinitionType(
      final String nameParam,
      final String controllerParam,
      final String inputMapperParam,
      final Attributes newAttributes) {
    this.name = nameParam;
    this.controller = controllerParam;
    this.inputMapper = inputMapperParam;
    this.controlDefinitionAttributes = newAttributes;
  }

  /**
   * Add a element.
   * @param elementType element to add
   */
  public void addElement(final ElementType elementType) {
    this.elements.add(elementType);
  }

  /**
   * Get new controller instance for this control definition.
   * @param nifty nifty
   * @return Controller
   */
  public Controller getControllerInstance(final Nifty nifty) {
    return ClassHelper.getInstance(controller, Controller.class);
  }

  /**
   * Get new NiftyInputMapping instance.
   * @return NiftyInputMapping
   */
  public NiftyInputMapping getInputMappingInstance() {
    NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMapper, NiftyInputMapping.class);
    if (inputMapping == null) {
      log.warning("unable to instance inputMapping - fall back to no mapping");
      inputMapping = new Default();
    }
    return inputMapping;
  }

  /**
   * get elements.
   * @return element collection
   */
  public Collection < ElementType > getElements() {
    return elements;
  }

  /**
   * process style attributes.
   * @param element element
   * @param styleHandler style handler
   * @param controlAttributes control attributes
   * @param screen screen
   * @param nifty nifty
   * @param registeredEffects effects
   * @param time time
   */
  public void applyControlAttributes(
      final Element element,
      final StyleHandler styleHandler,
      final Attributes controlAttributes,
      final Screen screen,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final TimeProvider time) {
    ElementType.applyControlStyle(
        element, styleHandler, controlDefinitionAttributes, controlAttributes, nifty, registeredEffects, time, screen);
    ElementType.applyControlParameters(
        element, controlAttributes, nifty, screen);
  }
}
