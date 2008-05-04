package de.lessvoid.nifty.elements.controls;

import java.util.Properties;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.elements.ControlController;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.MethodInvoker;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * A TextFieldControl.
 * @author void
 */
public class MenuItemControl implements ControlController {

  private Screen screen;
  private Element element;
  private MenuFocusHandler focusHandler;

  public MenuItemControl(final MenuFocusHandler focusHandlerParam) {
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
      final Screen newScreen,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener) {
    screen = newScreen;
    element = newElement;
  }

  /**
   * On start screen event.
   */
  public void onStartScreen() {
  }


  public void onGetFocus() {
    System.out.println("*** " + element.getId() + " *** onGetFocus");
  }

  public void onLostFocus() {
    System.out.println("*** " + element.getId() + " *** onLostFocus");
  }

  public void keyEvent(int eventKey, char keyEvent, boolean keyDown) {
    System.out.println("*** " + element.getId() + " *** keyEvent: " + eventKey + ", " + keyEvent + ", " + keyDown);
    if (keyDown) {
      if (eventKey == Keyboard.KEY_DOWN) {
        focusHandler.getNext(element).setFocus();
      } else if (eventKey == Keyboard.KEY_UP) {
        focusHandler.getPrev(element).setFocus();
      } else if (eventKey == Keyboard.KEY_RETURN) {
        element.onClick();
      }
    }
  }
  
  public void forward(final MethodInvoker controllerMethod) {
    
  }
}
