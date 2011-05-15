package de.lessvoid.nifty.controls.dropdown;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DropDownPopup<T> extends AbstractController {
  private Element element;
  @SuppressWarnings("deprecation") private DropDownControl<T> dropDownControl;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.element = element;
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @SuppressWarnings("deprecation")
  public void onStartScreen() {
    linkPopupToDropDownPosition(dropDownControl);
    dropDownControl.refresh();
  }

  @SuppressWarnings("deprecation")
  public void setDropDownElement(final DropDownControl<T> dropDownControl) {
    this.dropDownControl = dropDownControl;
    linkPopupToDropDownPosition(dropDownControl);
  }

  @SuppressWarnings("deprecation")
  private void linkPopupToDropDownPosition(final DropDownControl<T> dropDownControl) {
    Element panel = element.findElementByName("#panel");
    panel.setConstraintX(new SizeValue(dropDownControl.getElement().getX() + "px"));
    panel.setConstraintWidth(new SizeValue(dropDownControl.getWidth() + "px"));
    element.layoutElements();

    ListBoxControl listBox = element.findNiftyControl("#listBox", ListBoxControl.class);
    listBox.ensureWidthConstraints();
    panel.setConstraintY(new SizeValue(dropDownControl.getElement().getY() - listBox.getHeight() /* dropDownControl.getHeight() */ + "px"));
    panel.setConstraintHeight(new SizeValue(listBox.getHeight() + "px"));
    element.layoutElements();

    List<Effect> moveEffects = element.findElementByName("#panel").getEffects(EffectEventId.onStartScreen, Move.class);
    if (!moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().setProperty("offsetY", String.valueOf(listBox.getHeight()));
      moveEffects.get(0).getParameters().setProperty("mode", "fromOffset");
    }
    moveEffects = element.findElementByName("#panel").getEffects(EffectEventId.onEndScreen, Move.class);
    if (!moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().setProperty("offsetY", String.valueOf(listBox.getHeight()));
      moveEffects.get(0).getParameters().setProperty("mode", "toOffset");
    }
  }

  @SuppressWarnings("deprecation")
  public void close() {
    dropDownControl.close();
  }
}
