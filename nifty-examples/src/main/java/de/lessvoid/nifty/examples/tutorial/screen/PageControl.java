package de.lessvoid.nifty.examples.tutorial.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class PageControl implements Controller {
  private Element element;
  private FocusHandler focusHandler;
  private Screen screen;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters parameter) {
    element = newElement;
    screen = screenParam;
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    DropDown<String> dropDown = screen.findNiftyControl("dropDownControl", DropDown.class);
    dropDown.addItem("a");
    dropDown.addItem("b");
    dropDown.addItem("c");

    ListBox<String> listBox = screen.findNiftyControl("listBox", ListBox.class);
    listBox.addItem("a");
    listBox.addItem("b");
    listBox.addItem("c");
    listBox.addItem("d");
    listBox.addItem("e");
    listBox.addItem("f");
  }

  @Override
  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      element.onClickAndReleasePrimaryMouseButton();
      return true;
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
    }
    return false;
  }
}
