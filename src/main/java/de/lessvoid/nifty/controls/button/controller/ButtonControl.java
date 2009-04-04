package de.lessvoid.nifty.controls.button.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * ButtonControl.
 * @author void
 */
public class ButtonControl implements Controller {
  private Element element;
  private FocusHandler focusHandler;
  private Screen screen;

  /**
   * bind.
   * @param nifty nifty
   * @param newElement element
   * @param parameter parameter
   * @param listener listener
   */
  public void bind(
      final Nifty nifty,
      final Screen screenParam,
      final Element newElement,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    element = newElement;
    screen = screenParam;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  /**
   * inputEvent.
   * @param inputEvent inputEvent
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(element);
        if (nextElement.getParent().equals(element.getParent())) {
          nextElement.setFocus();
        }
      }
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
      if (focusHandler != null) {
        Element prevElement = focusHandler.getPrev(element);
        if (prevElement.getParent().equals(element.getParent())) {
          prevElement.setFocus();
        }
      }
    }
  }

  /**
   * onFocus.
   * @param getFocus getFocus
   */
  public void onFocus(final boolean getFocus) {
  }
}
