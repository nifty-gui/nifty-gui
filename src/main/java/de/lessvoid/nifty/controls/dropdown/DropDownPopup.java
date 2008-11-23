package de.lessvoid.nifty.controls.dropdown;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

public class DropDownPopup implements Controller {

  private Nifty nifty;
  private Element dropDownElement;

  public void bind(
      final Nifty niftyParam,
      final Element element,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    this.nifty = niftyParam;
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
  }

  public void onFocus(final boolean getFocus) {
  }

  public void onStartScreen(final Screen screen) {
  }

  public void setDropDownElement(final Element element) {
    dropDownElement = element;
  }

  public void close() {
    dropDownElement.getControl(DropDownControl.class).reset();
    nifty.closePopup("dropDownBoxSelectPopup");
  }
}
