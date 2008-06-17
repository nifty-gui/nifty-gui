package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.mapping.NoInputMapping;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;

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
   * style id.
   */
  private String styleId;

  /**
   * elements.
   */
  private Collection < ElementType > elements = new ArrayList < ElementType >();

  /**
   * Create new instance.
   * @param nameParam name
   * @param controllerParam controller
   * @param inputMapperParam inputMapperParam
   * @param newStyleId styleId (might be null when not set)
   */
  public RegisterControlDefinitionType(
      final String nameParam,
      final String controllerParam,
      final String inputMapperParam,
      final String newStyleId) {
    this.name = nameParam;
    this.controller = controllerParam;
    this.inputMapper = inputMapperParam;
    this.styleId = newStyleId;
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
      inputMapping = new NoInputMapping();
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
   * @param styleHandler style handler
   */
  public void controlProcessStyleAttributes(final StyleHandler styleHandler) {
    if (styleId != null) {
      for (ElementType elementType : elements) {
        elementType.controlProcessStyleAttributes(styleId, styleHandler);
      }
    }
  }

  /**
   * set style id.
   * @param newStyleId style id
   */
  public void setStyleId(final String newStyleId) {
    this.styleId = newStyleId;
  }
}
