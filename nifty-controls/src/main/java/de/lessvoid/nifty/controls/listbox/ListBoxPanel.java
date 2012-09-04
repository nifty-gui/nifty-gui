package de.lessvoid.nifty.controls.listbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxPanel<T> extends AbstractController {
  private Screen screen;
  private Element element;
  private FocusHandler focusHandler;
  private ListBoxImpl<T> listBox;
  private boolean hasFocus = false;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    screen = screenParam;
    element = elementParam;
    focusHandler = screen.getFocusHandler();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
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
      if (hasElements()) {
        int focusItemIndex = listBox.getFocusItemIndex();
        if (focusItemIndex < listBox.itemCount() - 1) {
          listBox.setFocusItemByIndex(focusItemIndex + 1);
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
      if (hasElements()) {
        int focusItemIndex = listBox.getFocusItemIndex();
        if (focusItemIndex > 0) {
          listBox.setFocusItemByIndex(focusItemIndex - 1);
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      if (hasElements()) {
        int focusItemIndex = listBox.getFocusItemIndex();
        if (focusItemIndex >= 0) {
          if (listBox.getSelection().contains(listBox.getFocusItem())) {
            listBox.deselectItemByIndex(focusItemIndex);
          } else {
            listBox.selectItemByIndex(focusItemIndex);
          }
        }
      }
    }
    return false;
  }

  private boolean hasElements() {
    return listBox.itemCount() > 0;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);

    hasFocus = getFocus;
    listBox.updateView();
  }

  public void setListBox(final ListBoxImpl<T> listBox) {
    this.listBox = listBox;
  }

  @Override
  public boolean hasFocus() {
    return hasFocus;
  }
}
