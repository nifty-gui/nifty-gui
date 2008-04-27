package de.lessvoid.nifty.loader.xpp3.elements;


import java.lang.reflect.Method;
import java.util.logging.Logger;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.tools.MethodResolver;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * InteractType.
 * @author void
 */
public class InteractType implements XmlElementProcessor {

  /**
   * logger.
   */
  private Logger log = Logger.getLogger(InteractType.class.getName());

  /**
   * the element.
   */
  private Element element;

  /**
   * the ScreenController.
   */
  private ScreenController controller;

  /**
   * create.
   * @param elementParam element
   * @param controllerParam ScreenController
   */
  public InteractType(final Element elementParam, final ScreenController controllerParam) {
    this.element = elementParam;
    this.controller = controllerParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    // onClick action
    Method method = getMethod(attributes, "onClick");
    if (method != null) {
      element.setOnClickMethod(method, controller, false);
      element.setVisibleToMouseEvents(true);
    }

    // onClick action
    method = getMethod(attributes, "onClickRepeat");
    if (method != null) {
      element.setOnClickMethod(method, controller, true);
      element.setVisibleToMouseEvents(true);
    }

    // onClick action
    method = getMethod(attributes, "onClickMouseMove");
    if (method != null) {
      element.setOnClickMouseMoveMethod(method, controller);
      element.setVisibleToMouseEvents(true);
    }

    // on click alternate
    if (attributes.isSet("onClickAlternateKey")) {
      element.setOnClickAlternateKey(attributes.get("onClickAlternateKey"));
      element.setVisibleToMouseEvents(true);
    }

    xmlParser.nextTag();
  }

  /**
   * Get Method.
   * @param attributes attributes
   * @param method method name
   * @return resolved Method
   */
  private Method getMethod(final Attributes attributes, final String method) {
    if (attributes.isSet(method)) {
      String methodName = attributes.get(method);
      Method onClickMethod = MethodResolver.findMethod(controller.getClass(), methodName);
      if (onClickMethod == null) {
        log.warning("method [" + methodName + "] not found in class [" + controller.getClass().getName() + "]");
        return null;
      }
      return onClickMethod;
    }
    return null;
  }
}
