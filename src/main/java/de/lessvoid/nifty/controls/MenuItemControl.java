package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

public class MenuItemControl implements Controller {
  private Element element;
  private FocusHandler focusHandler;

  public void bind(
      final Nifty nifty,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    element = newElement;
  }

  public void onStartScreen(final Screen newScreen) {
    focusHandler = newScreen.getFocusHandler();
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(element);
        nextElement.setFocus();
      }
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        Element prevElement = focusHandler.getPrev(element);
        prevElement.setFocus();
      }
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
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
    }
  }

  public void onFocus(final boolean getFocus) {
  }
}
