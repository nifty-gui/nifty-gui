package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

/**
 * A TextFieldControl.
 * @author void
 */
public class MenuItemControl implements Controller {

  /**
   * element.
   */
  private Element element;

  /**
   * focus handler.
   */
  private FocusHandler focusHandler;

  /**
   * Bind this controller to the given element.
   * @param nifty nifty
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Nifty nifty,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    element = newElement;
  }

  /**
   * On start screen event.
   * @param newScreen screen
   */
  public void onStartScreen(final Screen newScreen) {
    focusHandler = newScreen.getFocusHandler();
  }

  /**
   * on focus.
   * @param getFocus get or lose focus
   */
  public void onFocus(final boolean getFocus) {
    System.out.println("*** " + element.getId() + " *** onGetFocus");
  }

  /**
   * process InputEvent.
   * @param inputEvent input event to process
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
    }
  }
}
