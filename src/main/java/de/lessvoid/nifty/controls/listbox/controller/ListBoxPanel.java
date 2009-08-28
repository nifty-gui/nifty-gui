package de.lessvoid.nifty.controls.listbox.controller;

import java.util.ArrayList;
import java.util.List;
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
  private Screen screen;
  private Element element;
  private FocusHandler focusHandler;
  private int selectedItem;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    screen = screenParam;
    element = elementParam;
    selectedItem = -1;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
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
      if (hasElements()) {
        if (selectedItem < getElementCount() - 1) {
          changeSelection(selectedItem + 1);
        }
      }
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
      if (hasElements()) {
        if (selectedItem > 0) {
          changeSelection(selectedItem - 1);
        }
      }
    }
  }

  public void onFocus(boolean getFocus) {
  }

  public void changeSelection(final int newSelectedItemIndex) {
    updateSelection(newSelectedItemIndex);
  }

  public void changeSelection(final Element element) {
    changeSelection(getListBoxElements().indexOf(element));
  }

  public int getSelectedItemIndex() {
    return selectedItem;
  }

  public Element getSelectedElement() {
    return getListBoxElements().get(selectedItem);
  }

  public int getElementCount() {
    if (element != null) {
      return getListBoxElements().size();
    }
    return 0;
  }

  public boolean hasElements() {
    return getElementCount() > 0;
  }

  public void linkChildsToListBoxControl(final ListBoxControl listBoxControl) {
    if (!hasElements()) {
      return;
    }
    List < Element > elements = getListBoxElements();
    for (Element e : elements) {
      ListBoxItemController listBoxItem = e.getControl(ListBoxItemController.class);
      if (listBoxItem != null) {
        listBoxItem.setListBox(listBoxControl);
      }
    }
  }

  private Element getScrollElement() {
    if (element != null) {
      return element.getElements().get(0);
    }
    return null;
  }

  private void updateSelection(final int newSelectedItemIndex) {
    if (newSelectedItemIndex == -1) {
      if (selectedItem != -1) {
        deselect(selectedItem);
      }
    } else {
      if (selectedItem != -1) {
        deselect(selectedItem);
      }
      if (newSelectedItemIndex != -1) {
        select(newSelectedItemIndex);
      }
      selectedItem = newSelectedItemIndex;
    }
  }

  private void select(final int newSelectedItemIndex) {
    Element scrollElement = getScrollElement();
    if (scrollElement != null) {
      scrollElement.getElements().get(newSelectedItemIndex).startEffect(EffectEventId.onCustom, null);
    }
  }

  private void deselect(final int newSelectedItemIndex) {
    Element scrollElement = getScrollElement();
    if (scrollElement != null) {
      scrollElement.getElements().get(newSelectedItemIndex).stopEffect(EffectEventId.onCustom);
    }
  }

  private List < Element > getListBoxElements() {
    if (element.getElements().isEmpty()) {
      return new ArrayList < Element > ();
    }
    if (element.getElements().get(0).getElements().isEmpty()) {
      return new ArrayList < Element > ();
    }
    return element.getElements().get(0).getElements();
  }
}
