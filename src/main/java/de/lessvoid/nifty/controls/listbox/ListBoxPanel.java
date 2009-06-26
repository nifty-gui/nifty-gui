package de.lessvoid.nifty.controls.listbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxPanel implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private Element childRootElement;
  private FocusHandler focusHandler;
  private int selectedItem;
  private int maxSelectedItems;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    screen = screenParam;
    element = elementParam;
    selectedItem = 0;
    maxSelectedItems = 0;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();

    selectedItem = 0;
    maxSelectedItems = getScrollElement().getElements().size();
    updateSelection(selectedItem);
  }

  public void inputEvent(NiftyInputEvent inputEvent) {
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
      if (selectedItem < maxSelectedItems - 1) {
        changeSelection(selectedItem + 1);
      }
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
      if (selectedItem > 0) {
        changeSelection(selectedItem - 1);
      }
    }
  }
  
  public void onFocus(boolean getFocus) {
  }

  
  private Element getScrollElement() {
    if (element != null) {
      return element.getElements().get(0);
    }
    return null;
  }

  private void updateSelection(final int selectedItem) {
    Element scrollElement = getScrollElement();
    if (scrollElement != null) {
      scrollElement.getElements().get(selectedItem).startEffect(EffectEventId.onCustom, null);
      this.selectedItem = selectedItem;
    }
  }

  public void changeSelection(final int newSelectedItemIndex) {
    Element scrollElement = getScrollElement();
    if (scrollElement != null) {
      scrollElement.getElements().get(selectedItem).stopEffect(EffectEventId.onCustom);
      updateSelection(newSelectedItemIndex);
    }
  }
}
