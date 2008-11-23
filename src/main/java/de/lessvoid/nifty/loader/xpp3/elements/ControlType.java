package de.lessvoid.nifty.loader.xpp3.elements;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.tools.MethodResolver;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ImageType.
 * @author void
 */
public class ControlType extends ElementType {
  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(ControlType.class.getName());

  /**
   * name.
   * @required
   */
  private String name;

  /**
   * onChange.
   * @optional
   */
  private String onChange;

  /**
   * create it.
   * @param typeContext typeContext
   * @param nameParam name
   */
  public ControlType(
      final TypeContext typeContext,
      final String nameParam) {
    super(typeContext);
    this.name = nameParam;
  }

  /**
   * set on change.
   * @param onChangeParam resize hint
   */
  public void setOnChange(final String onChangeParam) {
    this.onChange = onChangeParam;
  }

  /**
   * create element.
   * @param parent parent
   * @param screen screen
   * @param screenController screenController
   * @param inputControlParam controlController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Screen screen,
      final NiftyInputControl inputControlParam,
      final ScreenController screenController) {

    RegisterControlDefinitionType controlDefinition = getControlDefinition();

    final Controller c = controlDefinition.getControllerInstance(typeContext.nifty);
    ControllerEventListener listener = null;

    // onClick action
    if (onChange != null) {
      final Method onChangeMethod = MethodResolver.findMethod(screenController.getClass(), onChange);
      if (onChangeMethod == null) {
        log.warning(
            "method [" + onChange + "] "
            + "not found in class [" + screenController.getClass().getName() + "]");
      } else {
        if (onChangeMethod != null) {
          listener = new ControllerEventListener() {
            public void onChangeNotify() {
              try {
                onChangeMethod.invoke(screenController, c);
              } catch (Exception e) {
                log.warning("ControllerEventListener with error: " + e.getMessage());
              }
            }
          };
        }
      }
    }

    final NiftyInputMapping inputMapping = controlDefinition.getInputMappingInstance();
    NiftyInputControl inputControl = new NiftyInputControl(c, inputMapping);

    // get very first child if available
    if (controlDefinition.getElements().size() == 1) {
      // create actual element
      ElementType w = controlDefinition.getElements().iterator().next();
      Element current = w.createElement(
          parent,
          screen,
          inputControl,
          screenController);
      current.setId(getAttributes().getId());
      super.addElementAttributes(
          current,
          screen,
          screenController,
          inputControlParam,
          inputControl);
      controlDefinition.applyControlAttributes(
          current,
          typeContext.styleHandler,
          getAttributes().getSrcAttributes(),
          screen,
          typeContext.nifty,
          typeContext.registeredEffects,
          typeContext.time);
      Properties props = null;
      if (getAttributes().getSrcAttributes() != null) {
        props = getAttributes().getSrcAttributes().createProperties();
      }
      if (c != null) {
        c.bind(typeContext.nifty, current, props, listener, controlDefinition.getControlDefinitionAttributes());
      }
      return current;
    }

    return null;
  }

  public RegisterControlDefinitionType getControlDefinition() {
    RegisterControlDefinitionType controlDefinition = typeContext.registeredControls.get(name);
    if (controlDefinition == null) {
      log.warning("found no control definition for [" + name + "]");
      return null;
    }
    return controlDefinition;
  }
}
