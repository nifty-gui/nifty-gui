package de.lessvoid.nifty.controls;

import java.util.Properties;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.MethodInvoker;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * A TextFieldControl.
 * @author void
 */
public class MenuItemControl implements Controller {

  private Screen screen;
  private Element element;
  private FocusHandler focusHandler;

  public MenuItemControl(final FocusHandler focusHandlerParam) {
    focusHandler = focusHandlerParam;
  }
  /**
   * Bind this controller to the given element.
   * @param newScreen the new nifty to set
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      Nifty nifty,
      final Screen newScreen,
      final Element newElement,
      final Properties properties, final ControllerEventListener newListener) {
    screen = newScreen;
    element = newElement;
  }

  /**
   * On start screen event.
   */
  public void onStartScreen() {
  }


  public void onFocus(boolean getFocus) {
    System.out.println("*** " + element.getId() + " *** onGetFocus");
  }

  public void onLostFocus() {
    System.out.println("*** " + element.getId() + " *** onLostFocus");
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
    } else if (inputEvent == NiftyInputEvent.SubmitText) {
      element.onClick();
    }
  }

  public void forward(final MethodInvoker controllerMethod) {
    
  }
}
