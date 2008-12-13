package de.lessvoid.nifty.controls.dropdown;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class DropDownControl implements Controller {
  private Logger log = Logger.getLogger(DropDownControl.class.getName());
  private Nifty nifty;
  private Element element;
  private boolean alreadyOpen = false;
  private DropDownModel dropDownModel = new DropDownModel();
  private Attributes controlDefinitionAttributes;
  private FocusHandler focusHandler;
  private Screen screen;

  public void bind(
      final Nifty niftyParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributesParam) {
    nifty = niftyParam;
    element = newElement;
    controlDefinitionAttributes = controlDefinitionAttributesParam;
  }

  public void onStartScreen(final Screen screenParam) {
    screen = screenParam;
    focusHandler = screen.getFocusHandler();
  }

  public void onFocus(final boolean getFocus) {
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      screen.nextElementFocus();
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        focusHandler.getPrev(element).setFocus();
      }
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
    }
  }

  public void dropDownClicked() {
    log.info("dropDownClicked() - " + alreadyOpen);
    if (alreadyOpen) {
      return;
    }
    alreadyOpen = true;
    Element popupLayer = nifty.createPopup("dropDownBoxSelectPopup");
    log.info("popupLayer: " + popupLayer);
    ElementType.applyControlStyle(
        popupLayer,
        nifty.getStyleHandler(),
        controlDefinitionAttributes.get("style"),
        element.getElementType().getAttributes().getStyle(),
        nifty,
        nifty.getLoader().getRegisteredEffects(),
        nifty.getTimeProvider(),
        nifty.getCurrentScreen());
    Element popup = popupLayer.findElementByName("dropDownList");
    log.info("popup: " + popup);
    popup.setConstraintX(new SizeValue(element.getX() + "px"));
    popup.setConstraintY(new SizeValue(element.getY() + element.getHeight() + "px"));
    popup.setConstraintWidth(new SizeValue(element.getWidth() + "px"));

    for (Element e : popup.getElements()) {
      nifty.removeElement(nifty.getCurrentScreen(), e);
    }

    dropDownModel.initialize(nifty, nifty.getCurrentScreen(), popup);

    log.info("a");
    nifty.addControls();
    log.info("b");

    int maxHeight = 0;
    for (Element child : popup.getElements()) {
      child.getControl(DropDownControlItem.class).setDropDownControl(element);
      maxHeight += child.getHeight();
    }
    popup.layoutElements();
    popup.setConstraintHeight(new SizeValue(maxHeight + "px"));
    popupLayer.getControl(DropDownPopup.class).setDropDownElement(element);
    nifty.showPopup(nifty.getCurrentScreen(), "dropDownBoxSelectPopup");
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

  private void changeSelectedItem(final String selectedItem) {
    TextRenderer text = element.findElementByName("text").getRenderer(TextRenderer.class);
    text.setText(selectedItem);
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
}
