package de.lessvoid.nifty.controls.dropdown;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

public class DropDownControlItem implements Controller {

  private Nifty nifty;
  private Element dropDownControlItemElement;
  private Element dropDownControlElement;

  public void bind(
      final Nifty niftyParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    dropDownControlItemElement = newElement;
  }

  public void onStartScreen(final Screen newScreen) {
  }

  public void onFocus(final boolean getFocus) {
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
  }

  public void dropDownItemClicked() {
    DropDownControl dropDownControl = dropDownControlElement.getControl(DropDownControl.class);
    dropDownControl.reset();
    dropDownControl.setSelectedItem(dropDownControlItemElement.getRenderer(TextRenderer.class).getOriginalText());
    nifty.closePopup("dropDownBoxSelectPopup");
  }

  public void setDropDownControl(final Element element) {
    this.dropDownControlElement = element;
  }
}
