package de.lessvoid.nifty.examples.tutorial.screen;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class PageControl implements Controller {
  private Element element;
  private FocusHandler focusHandler;
  private Screen screen;

  public void bind(
      final Nifty nifty,
      final Screen screenParam,
      final Element newElement,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    element = newElement;
    screen = screenParam;
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
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

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  public void onFocus(final boolean getFocus) {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(element);
        if (nextElement.getParent().equals(element.getParent())) {
          nextElement.setFocus();
          return true;
        }
      }
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
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
