package de.lessvoid.nifty.controls.dropdown.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class DropDownControlItem extends AbstractController {
    private Nifty nifty;

    private Screen screen;

    private Element dropDownControlItemElement;

    private FocusHandler focusHandler;

    private DropDownControl dropDownControl;

    public void bind(final Nifty niftyParam, final Screen screenParam, final Element newElement,
            final Properties properties, final ControllerEventListener newListener,
            final Attributes controlDefinitionAttributes) {
        nifty = niftyParam;
        screen = screenParam;
        dropDownControlItemElement = newElement;
    }

    public void onStartScreen() {
        focusHandler = screen.getFocusHandler();
    }

    @Override
    public void onFocus(final boolean getFocus) {
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyInputEvent.NextInputElement) {
            focusHandler.getNext(dropDownControlItemElement).setFocus();
            return true;
        }
        else if (inputEvent == NiftyInputEvent.PrevInputElement) {
            focusHandler.getPrev(dropDownControlItemElement).setFocus();
            return true;
        }
        else if (inputEvent == NiftyInputEvent.Activate) {
            dropDownItemClicked();
            return true;
        }
        else if (inputEvent == NiftyInputEvent.Escape) {
            dropDownControl.reset();
            nifty.closePopup("dropDownBoxSelectPopup");
            return true;
        }
        return false;
    }

    public void dropDownItemClicked() {
        dropDownControl.reset();
        dropDownControl.setSelectedItem(dropDownControlItemElement.getRenderer(TextRenderer.class)
                .getOriginalText());
        dropDownControl.notifyObservers();
        nifty.closePopup("dropDownBoxSelectPopup");
    }

    public void setDropDownControl(final Element element) {
        dropDownControl = element.getControl(DropDownControl.class);
    }
}
