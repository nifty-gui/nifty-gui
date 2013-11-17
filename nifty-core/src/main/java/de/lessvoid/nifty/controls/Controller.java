package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * Controller.
 * @author void
 */
public interface Controller {

  /**
   * Bind this Controller to a certain element.
   *
   * <b>Migration note for 1.3.x users</b>
   * <ul>
   * <li>controlDefinitionAttributes parameter has been removed. The data already was a part of the existing "parameter"
   * parameter</li>
   * <li>The "parameter" was changed from a Properties instance to a ControlParameters instance which works the same as
   * the Properties instance but is read-only and has additional methods to access values directly as booleans or ints.
   * </ul>
   *
   * @param nifty the Nifty instance
   * @param screen the Screen this Controller exists in
   * @param element the actual Element 
   * @param parameter this contains all attributes of the controlDefinition as well as attributes from the control tag
   * (where you actually placed the control). Please note that the controlDefinition parameters are applied first. Which
   * means that attributes in the control tag can overwrite the defaults of the controlDefinition.
   */
  void bind(
      Nifty nifty,
      Screen screen,
      Element element,
      Parameters parameter);

  /**
   * Init the Controller. You can assume that bind() has been called for all other controls on the screen.
   * @param parameter this contains all attributes of the controlDefinition as well as attributes from the control tag
   * (where you actually placed the control). Please note that the controlDefinition parameters are applied first. Which
   * means that attributes in the control tag can overwrite the defaults of the controlDefinition.
   */
  void init(Parameters parameter);

  /**
   * Called when the screen is started.
   */
  void onStartScreen();

  /**
   * This controller gets the focus.
   * @param getFocus get focus (true) or loose focus (false)
   */
  void onFocus(boolean getFocus);

  /**
   * input event.
   * @param inputEvent the NiftyInputEvent to process
   * @return true, the event has been handled and false, the event has not been handled
   */
  boolean inputEvent(NiftyInputEvent inputEvent);
}
