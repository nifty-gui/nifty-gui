package de.lessvoid.nifty.loader.xpp3.elements;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.tools.MethodResolver;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

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
   * @param nameParam name
   */
  public ControlType(final String nameParam) {
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

    RegisterControlDefinitionType controlDefinition = registeredControls.get(name);
    if (controlDefinition == null) {
      log.warning("found no control definition for [" + name + "]");
      return null;
    }

    final Controller c = controlDefinition.getControllerInstance(nifty);
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

    // get very first child if available
    if (controlDefinition.getElements().size() == 1) {
      ElementType w = controlDefinition.getElements().iterator().next();
      Element current = w.createElement(
          parent,
          nifty,
          screen,
          c,
          registeredEffects,
          registeredControls,
          time);
      c.bind(screen, current, null, listener);
      return current;
    }

    return null;
  }
}
