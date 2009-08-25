package de.lessvoid.nifty.controls.listbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxItemController implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element listBoxControlItemElement;
  private FocusHandler focusHandler;
  private ListBoxControl listBoxControl;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    screen = screenParam;
    listBoxControlItemElement = newElement;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  public void onFocus(final boolean getFocus) {
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(listBoxControlItemElement).setFocus();
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(listBoxControlItemElement).setFocus();
    } else if (inputEvent == NiftyInputEvent.Activate) {
      listBoxItemClicked();
    }
  }

  public void listBoxItemClicked() {
    System.out.println("listBoxItemClicked");
  }
}
