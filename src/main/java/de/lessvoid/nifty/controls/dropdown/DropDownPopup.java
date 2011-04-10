package de.lessvoid.nifty.controls.dropdown;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DropDownPopup<T> extends AbstractController {
  private static Logger log = Logger.getLogger(DropDownPopup.class.getName());

  private Nifty nifty;
  private Element element;
  private DropDownControl<T> dropDownControl;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    nifty = niftyParam;
    this.element = element;
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  public void onStartScreen() {
    dropDownControl.refresh();
  }

  public void setDropDownElement(final DropDownControl<T> dropDownControl) {
    this.dropDownControl = dropDownControl;
    Element panel = element.findElementByName("#panel");
    panel.setConstraintX(new SizeValue(dropDownControl.getElement().getX() + "px"));
    panel.setConstraintY(new SizeValue(dropDownControl.getElement().getY() +dropDownControl.getHeight() + "px"));
    panel.setConstraintWidth(new SizeValue(dropDownControl.getWidth() + "px"));
    element.layoutElements();
  }

  public void close() {
    dropDownControl.close();
  }
}
