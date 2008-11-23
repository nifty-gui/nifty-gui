package de.lessvoid.nifty.controls.button;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

/**
 * ButtonControl.
 * @author void
 */
public class ButtonControl implements Controller {

  /**
   * the element.
   */
  private Element element;

  /**
   * focus handler.
   */
  private FocusHandler focusHandler;

  /**
   * bind.
   * @param nifty nifty
   * @param newElement element
   * @param parameter parameter
   * @param listener listener
   */
  public void bind(
      final Nifty nifty,
      final Element newElement,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    this.element = newElement;
  }

  /**
   * inputEvent.
   * @param inputEvent inputEvent
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(element).setFocus();
      }
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        focusHandler.getPrev(element).setFocus();
      }
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
    }
  }

  /**
   * onFocus.
   * @param getFocus getFocus
   */
  public void onFocus(final boolean getFocus) {
  }

  /**
   * onStartScreen.
   * @param screen screen
   */
  public void onStartScreen(final Screen screen) {
    this.focusHandler = screen.getFocusHandler();
  }
}
