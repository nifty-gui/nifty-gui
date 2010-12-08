package de.lessvoid.nifty.controls.listbox.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxPanel<T> extends AbstractController {
    private Screen screen;

    private Element element;

    private FocusHandler focusHandler;

    private ListBox<T> listBox;

    public void bind(final Nifty niftyParam, final Screen screenParam, final Element elementParam,
            final Properties parameter, final ControllerEventListener listener,
            final Attributes controlDefinitionAttributes) {
        screen = screenParam;
        element = elementParam;
    }

    public void onStartScreen() {
        focusHandler = screen.getFocusHandler();
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyInputEvent.NextInputElement) {
            if (focusHandler != null) {
                Element nextElement = focusHandler.getNext(element);
                nextElement.setFocus();
                return true;
            }
        }
        else if (inputEvent == NiftyInputEvent.PrevInputElement) {
            if (focusHandler != null) {
                Element prevElement = focusHandler.getPrev(element);
                prevElement.setFocus();
                return true;
            }
        }
        else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
            if (hasElements()) {
                int focusItemIndex = listBox.getFocusItemIndex();
                if (focusItemIndex < listBox.itemCount() - 1) {
                    listBox.setFocusItemByIndex(focusItemIndex + 1);
                    return true;
                }
            }
        }
        else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
            if (hasElements()) {
                int focusItemIndex = listBox.getFocusItemIndex();
                if (focusItemIndex > 0) {
                    listBox.setFocusItemByIndex(focusItemIndex - 1);
                    return true;
                }
            }
        }
        else if (inputEvent == NiftyInputEvent.Activate) {
            if (hasElements()) {
                int focusItemIndex = listBox.getFocusItemIndex();
                if (focusItemIndex >= 0) {
                    if (listBox.getSelection().contains(listBox.getFocusItem())) {
                        listBox.deselectItemByIndex(focusItemIndex);
                    }
                    else {
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
    }

    public void setListBox(final ListBox<T> listBox) {
        this.listBox = listBox;
    }
}
