package de.lessvoid.nifty.controls.dropdown.controller;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.NiftyObservable;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DropDownControl implements Controller {
  private Nifty nifty;
  private Element element;
  private boolean alreadyOpen = false;
  private DropDownModel dropDownModel = new DropDownModel();
  private FocusHandler focusHandler;
  private Screen screen;
  private NiftyObservable observable = new NiftyObservable();

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributesParam) {
    nifty = niftyParam;
    screen = screenParam;
    element = newElement;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  public void onFocus(final boolean getFocus) {
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.Activate) {
      dropDownClicked();
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
      setSelectedItemIdx(dropDownModel.gotoPrevItem());
      notifyObservers();
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
      setSelectedItemIdx(dropDownModel.gotoNextItem());
      notifyObservers();
    }
  }

  public void dropDownClicked() {
    if (alreadyOpen) {
      return;
    }
    alreadyOpen = true;
    Element popupLayer = nifty.createPopup("dropDownBoxSelectPopup");
    Element popup = popupLayer.findElementByName("dropDownList");
    popup.setConstraintX(new SizeValue(element.getX() + "px"));
    popup.setConstraintY(new SizeValue(element.getY() + element.getHeight() + "px"));
    popup.setConstraintWidth(new SizeValue(element.getWidth() + "px"));

    for (Element e : popup.getElements()) {
      nifty.removeElement(nifty.getCurrentScreen(), e);
    }

    dropDownModel.initialize(nifty, nifty.getCurrentScreen(), popup, element.getElementType().getAttributes().get("style"));
    nifty.addControlsWithoutStartScreen();

    int maxHeight = getMaxHeight(popup);
    popup.layoutElements();
    popup.setConstraintHeight(new SizeValue(maxHeight + "px"));
    popupLayer.getControl(DropDownPopup.class).setDropDownElement(element);

    Element selectedElement = convertSelectedItemToElement(popup, dropDownModel.getSelectedItemIdx());
    nifty.showPopup(nifty.getCurrentScreen(), "dropDownBoxSelectPopup", selectedElement);
  }

  int getMaxHeight(final Element popup) {
    int maxHeight = 0;
    for (Element child : popup.getElements()) {
      child.getControl(DropDownControlItem.class).setDropDownControl(element);
      maxHeight += child.getHeight();
    }
    return maxHeight;
  }

  private Element convertSelectedItemToElement(final Element popup, final int selectedItemIdx) {
    if (selectedItemIdx == -1) {
      return null;
    }

    for (int idx = 0; idx < popup.getElements().size(); idx++) {
      if (idx == selectedItemIdx) {
        return popup.getElements().get(idx);
      }
    }

    return null;
  }

  public void reset() {
    alreadyOpen = false;
  }

  public void addItem(final String item) {
    dropDownModel.addItem(item);
  }

  public void setSelectedItemIdx(final int idx) {
    dropDownModel.setSelectedItemIdx(idx);
    changeSelectedItem(dropDownModel.getSelectedItem());
  }

  public void setSelectedItem(final String text) {
    dropDownModel.setSelectedItem(text);
    changeSelectedItem(dropDownModel.getSelectedItem());
  }

  public String getSelectedItem() {
    return dropDownModel.getSelectedItem();
  }

  public int getSelectedItemIdx() {
    return dropDownModel.getSelectedItemIdx();
  }

  public void clear() {
    dropDownModel.clear();
  }

  public void addNotify(final DropDownControlNotify notity) {
    observable.addObserver(new Observer() {
      public void update(final Observable o, final Object arg) {
        notity.dropDownSelectionChanged(DropDownControl.this);
      }
    });
  }

  public void removeAllNotifies() {
    observable.deleteObservers();
  }

  private void changeSelectedItem(final String selectedItem) {
    TextRenderer text = element.findElementByName("text").getRenderer(TextRenderer.class);
    text.setText(selectedItem);
  }

  public void notifyObservers() {
    observable.setChanged();
    observable.notifyObservers();
  }
}
