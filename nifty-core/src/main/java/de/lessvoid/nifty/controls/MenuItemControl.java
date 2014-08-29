package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class MenuItemControl extends AbstractController {
  private Screen screen;
  private FocusHandler focusHandler;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters properties) {
    bind(newElement);
    screen = screenParam;
  }

  @Override
  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    Element element = getElement();
    if (element == null) {
      return false;
    }
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(element);
        nextElement.setFocus();
        return true;
      }
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        Element prevElement = focusHandler.getPrev(element);
        prevElement.setFocus();
        return true;
      }
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(element);
        if (nextElement.getParent().equals(element.getParent())) {
          nextElement.setFocus();
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
      if (focusHandler != null) {
        Element prevElement = focusHandler.getPrev(element);
        if (prevElement.getParent().equals(element.getParent())) {
          prevElement.setFocus();
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      element.onClickAndReleasePrimaryMouseButton();
      return true;
    }
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }
}
