package de.lessvoid.nifty.loaderv2;

import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.tools.Factory;
import de.lessvoid.xml.tools.ClassHelper;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is used to create Controllers other than through the empty
 * constructor of a controller. If a factory is registered to a controller class
 * type, it will be responsible to create the new instance of the controller.
 * Otherwise, we try to instantiate the controller using an empty constructor.
 *
 * @author jfrenaud
 */
public class ControllerFactory {

  /**
   * The map containing the registered controllerFactories.
   */
  @Nonnull
  private final Map<String, Factory<? extends Controller>> registeredControllersFactories = new HashMap<String, Factory<? extends Controller>>();

  /**
   * Register factory to create a controllers.
   * <p>
   * @param <C> any type of Controller
   * @param controllerFactory The factory used to create a controller type.
   * @param controllerClass The class type that the factory will create.
   */
  public <C extends Controller> void registerFactory(@Nonnull final Factory<C> controllerFactory, @Nonnull final Class<C> controllerClass) {
    registeredControllersFactories.put(controllerClass.getName(), controllerFactory);
  }

  /**
   * Unregister a factory previously registered, where the factory match the
   * provided class type.
   *
   * @param <C> any type of Controller
   * @param controllerClass The class type that the factory to remove can
   * create.
   */
  public <C extends Controller> void unregisterFactory(@Nonnull final Class<C> controllerClass) {
    registeredControllersFactories.remove(controllerClass.getName());
  }

  /**
   * Creates a Controller given a fully qualified class name. If a factory is
   * registered to a controller class type, it will be responsible to create the
   * new instance of the controller. Otherwise, we try to instantiate the
   * controller using an empty constructor.
   *
   * @param controllerClassName a fully qualified controller class name.
   * @return a new Controller, or null if not able to create one.
   */
  @Nullable
  public Controller create(@Nullable final String controllerClassName) {
    if (controllerClassName == null) {
      return null;
    }
    if (registeredControllersFactories.containsKey(controllerClassName)) {
      return registeredControllersFactories.get(controllerClassName).createNew();
    } else {
      return ClassHelper.getInstance(controllerClassName, Controller.class);
    }
  }

}
