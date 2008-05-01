package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Controller;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * RegisterControlDefinitionType.
 * @author void
 */
public class RegisterControlDefinitionType {
  /**
   * logger.
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
   * elements.
   */
  private Collection < ElementType > elements = new ArrayList < ElementType >();

  /**
   * Create new instance.
   * @param nameParam name
   * @param controllerParam controller
   */
  public RegisterControlDefinitionType(final String nameParam, final String controllerParam) {
    this.name = nameParam;
    this.controller = controllerParam;
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
  public de.lessvoid.nifty.elements.Controller getControllerInstance(final Nifty nifty) {
    return getController(nifty, controller);
  }

  /**
   * dynamically load the given class, create and return a new instance.
   * @param niftyParent niftyParent
   * @param controllerClass controllerClass
   * @return new ScreenController instance or null
   */
  private static Controller getController(
      final Nifty niftyParent,
      final String controllerClass) {
    try {
      Class < ? > cls = ControlType.class.getClassLoader().loadClass(controllerClass);
      if (Controller.class.isAssignableFrom(cls)) {
        return (Controller) cls.newInstance();
      } else {
        log.warning(
            "given screenController class ["
            + controllerClass + "] does not implement [" + ScreenController.class.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + controllerClass + "] could not be instanziated");
    }
    return null;
  }

  /**
   * get elements.
   * @return element collection
   */
  public Collection < ElementType > getElements() {
    return elements;
  }
}
